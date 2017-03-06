package io.gxu.anymall.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DBConnectionPool {

	private String dbUser;
	private String dbUrl;
	private String dbPass;
	private String jdbcDriverClass;

	private int maxConnections = 10;
	private int connectionIncrement = 5;
	private int initialConnections = 5;
	private final List<PooledConnection> connectionSet;

	private transient Logger log = Logger.getLogger(DBConnectionPool.class);

	public DBConnectionPool(String jdbcDriver, String dbUrl, String dbUser,
			String dbPass) throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.jdbcDriverClass = jdbcDriver;

		this.connectionSet = new ArrayList<PooledConnection>();
		this.initPool();
	}

	private synchronized void initPool() throws ClassNotFoundException,
			SQLException, InstantiationException, IllegalAccessException {
		log.info(String.format(
				"��ʼ�����ݿ����ӳ�: ������� %s, ��ʼ������ %s, ���������� %s, ���ݿ����� %s, ���ݿ�URL %s",
				maxConnections, initialConnections, connectionIncrement,
				jdbcDriverClass, dbUrl));

		// ע�����ݿ�����
		Driver driver = (Driver) Class.forName(jdbcDriverClass).newInstance();
		DriverManager.registerDriver(driver);

		// ������ʼ����
		this.createConnections(initialConnections);
	}

	private void createConnections(int nConnections) {
		log.info(String.format("׼������ %s ��������", nConnections));

		int nCreate = 0;
		for (int i = 0; i < nConnections; i++) {
			if (connectionSet.size() > maxConnections) {
				log.warn("�������ﵽ���ޣ��ܾ���������");
				break;
			}
			try {
				this.connectionSet.add(new PooledConnection(newConnection()));
				nCreate++;
			} catch (SQLException e) {
				log.error(String.format("��������ʧ�ܣ�%s", e.getMessage()));
				e.printStackTrace();
			}
		}

		log.info(String.format("�ɹ����� %s �����ӣ�ʧ��  %s ��", nCreate, nConnections
				- nCreate));
	}

	private Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		return conn;
	}

	public PooledConnection getConnection() {
		try {
			return getConnection(Long.MAX_VALUE);
		} catch (TimeoutException e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized PooledConnection getConnection(long timeout)
			throws TimeoutException {
		long t1 = System.currentTimeMillis() + timeout;

		while (System.currentTimeMillis() < t1) {
			PooledConnection conn = getFreeConnection();
			if (conn == null) {
				waitForConnection();
				continue;
			}
			return conn;
		}
		log.error(String.format("��ȡ���ӳ�ʱ %s", timeout));
		throw new TimeoutException("��ȡ���ӳ�ʱ: " + timeout);
	}

	private void waitForConnection() {
		synchronized (this) {
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private PooledConnection getFreeConnection() {
		for (int i = connectionSet.size() - 1; i >= 0; --i) {
			PooledConnection conn = connectionSet.get(i);
			if (!conn.isLocked()) {
				conn.lock();

				// ��ȡ���������Ѿ�ʧЧ�����½����ӡ�
				if (!validateConnection(conn.getConnection())) {
					try {
						conn.setConnection(newConnection());
					} catch (SQLException e) {
						log.error(String.format("����������ʧ�ܣ�%s", e.getMessage()));
						e.printStackTrace();
						continue;
					}
				}
				return conn;
			}
		}
		return null;
	}

	private boolean validateConnection(Connection conn) {
		try {
			return conn.isValid(2000);
		} catch (Exception e) {
			log.error(String.format("��֤���ݿ�����״̬ʱ��������%s", e.getMessage()));
			e.printStackTrace();
			return false;
		}
	}

	public void returnConnection(PooledConnection conn) {
		if (connectionSet.contains(conn)) {
			conn.release();
		}
	}

	public static class PooledConnection {
		private Connection conn;
		private boolean locked;

		private PooledConnection(Connection conn) {
			this.conn = conn;
		}

		private boolean isLocked() {
			return locked;
		}

		private void lock() {
			this.locked = true;
		}

		private void release() {
			this.locked = false;
		}

		private void setConnection(Connection conn) {
			this.conn = conn;
		}

		private Connection getConnection() {
			return conn;
		}

		public void close() {
			this.locked = false;
		}

		public ResultSet execQuery(String sql) throws SQLException {
			return conn.createStatement().executeQuery(sql);
		}

		public ResultSet execQuery(String sql, Object[] params)
				throws SQLException {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
 			}
			return stmt.executeQuery();
		}

		public int execUpdate(String sql) throws SQLException {
			return conn.createStatement().executeUpdate(sql);
		}

		public int execUpdate(String sql, Object[] params) throws SQLException {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			return stmt.executeUpdate();
		}

		public int execCallUpdate(String sql, Object[] params)
				throws SQLException {
			CallableStatement call = conn.prepareCall(sql);
			for (int i = 0; i < params.length; i++) {
				call.setObject(i + 1, params[0]);
			}
			return call.executeUpdate();
		}

		public ResultSet execCallQuery(String sql, Object[] params)
				throws SQLException {
			CallableStatement call = conn.prepareCall(sql);
			for (int i = 0; i < params.length; i++) {
				call.setObject(i + 1, params[0]);
			}
			return call.executeQuery();
		}
	}
}
