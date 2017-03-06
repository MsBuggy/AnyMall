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
				"初始化数据库连接池: 最大连接 %s, 初始连接数 %s, 自增连接数 %s, 数据库驱动 %s, 数据库URL %s",
				maxConnections, initialConnections, connectionIncrement,
				jdbcDriverClass, dbUrl));

		// 注册数据库驱动
		Driver driver = (Driver) Class.forName(jdbcDriverClass).newInstance();
		DriverManager.registerDriver(driver);

		// 创建初始连接
		this.createConnections(initialConnections);
	}

	private void createConnections(int nConnections) {
		log.info(String.format("准备创建 %s 个新连接", nConnections));

		int nCreate = 0;
		for (int i = 0; i < nConnections; i++) {
			if (connectionSet.size() > maxConnections) {
				log.warn("连接数达到上限，拒绝创建连接");
				break;
			}
			try {
				this.connectionSet.add(new PooledConnection(newConnection()));
				nCreate++;
			} catch (SQLException e) {
				log.error(String.format("创建连接失败：%s", e.getMessage()));
				e.printStackTrace();
			}
		}

		log.info(String.format("成功创建 %s 个连接，失败  %s 个", nCreate, nConnections
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
		log.error(String.format("获取连接超时 %s", timeout));
		throw new TimeoutException("获取连接超时: " + timeout);
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

				// 获取到的连接已经失效，需新建连接。
				if (!validateConnection(conn.getConnection())) {
					try {
						conn.setConnection(newConnection());
					} catch (SQLException e) {
						log.error(String.format("创建新连接失败：%s", e.getMessage()));
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
			log.error(String.format("验证数据库连接状态时发生错误：%s", e.getMessage()));
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
