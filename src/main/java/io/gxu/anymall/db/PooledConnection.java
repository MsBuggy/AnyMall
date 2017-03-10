package io.gxu.anymall.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PooledConnection {
	private Connection conn;
	private boolean locked;

	PooledConnection(Connection conn) {
		this.conn = conn;
	}

	boolean isLocked() {
		return locked;
	}

	void lock() {
		this.locked = true;
	}

	void release() {
		this.locked = false;
	}

	void setConnection(Connection conn) {
		this.conn = conn;
	}

	Connection getConnection() {
		return conn;
	}

	public void close() {
		this.locked = false;
	}

	private List<Object[]> readResultSet(ResultSet rs) throws SQLException {
		if (rs == null) {
			return null;
		}
		List<Object[]> array = new ArrayList<Object[]>();

		ResultSetMetaData metadata = rs.getMetaData();
		int nColumn = metadata.getColumnCount();

		//添加表头
		Object[] columnNames = new Object[nColumn];
		for (int i = 1; i <= nColumn; i++) {
			columnNames[i - 1] = metadata.getColumnName(i);
		}
		array.add(columnNames);
		
		//读取数据
		while (rs.next()) {
			Object[] row = new Object[nColumn];
			for (int i = 0; i < nColumn; i++) {
				row[i] = rs.getObject(i + 1);
			}
			array.add(row);
		}
		return array;
	}

	public List<Object[]> execQuery(String sql) throws SQLException {
		return readResultSet(conn.createStatement().executeQuery(sql));
	}

	public List<Object[]> execQuery(String sql, Object[] params)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
		}
		return readResultSet(stmt.executeQuery());
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

	public int execCallUpdate(String sql, Object[] params) throws SQLException {
		CallableStatement call = conn.prepareCall(sql);
		for (int i = 0; i < params.length; i++) {
			call.setObject(i + 1, params[0]);
		}
		return call.executeUpdate();
	}

	public List<Object[]> execCallQuery(String sql, Object[] params)
			throws SQLException {
		CallableStatement call = conn.prepareCall(sql);
		for (int i = 0; i < params.length; i++) {
			call.setObject(i + 1, params[0]);
		}
		return readResultSet(call.executeQuery());
	}
}