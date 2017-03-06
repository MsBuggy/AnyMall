package io.gxu.anymall.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.gxu.anymall.dao.DBConnectionPool;
import io.gxu.anymall.dao.DBConnectionPool.PooledConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBConnectionPoolTest {

	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	// 自动连接，使用unicode编码，设定时区
	private String dbUrl = "jdbc:mysql://localhost:3306/anymall?autoReconnect=true&useUnicode=true&serverTimezone=Asia/Shanghai";
	private String dbUser = "anymall_admin";
	private String dbPass = "llamyna";
	private PooledConnection conn;
	private DBConnectionPool pool;

	@Before
	public void createPool() throws Exception {
		pool = new DBConnectionPool(jdbcDriver, dbUrl, dbUser, dbPass);
		conn = pool.getConnection(1000);
	}

	@After
	public void destroy() {
		conn.close();
	}

	@Test(timeout = 10000)
	public void testQuery() throws SQLException {
		ResultSet rs = conn.execQuery("select * from am_user_user");
		assert (rs != null);

		System.out.println("query result: ");
		int nColumn = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= nColumn; i++) {
			System.out.print(rs.getMetaData().getColumnName(i) + "\t");
		}
		System.out.println();

		while (rs.next()) {
			for (int i = 1; i <= nColumn; i++) {
				System.out.print(rs.getObject(i) + "\t");
			}
			System.out.println();
		}
	}

	@Test(timeout = 10000)
	public void testQueryWithParams() throws SQLException {
		Object[] params = new Object[] { 1 };
		ResultSet rs = conn.execQuery(
				"select * from am_user_user where gender=?", params);

		assert (rs != null);

		System.out.println("query with param result: ");
		int nColumn = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= nColumn; i++) {
			System.out.print(rs.getMetaData().getColumnName(i) + "\t");
		}
		System.out.println();

		while (rs.next()) {
			for (int i = 1; i <= nColumn; i++) {
				System.out.print(rs.getObject(i) + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void testUpdate() throws SQLException {
		Object[] params = new Object[] { "1999-09-01", 3 };
		int n = conn.execUpdate(
				"update am_user_user set date_of_birth=? where id=?", params);
		System.out.println("update result: " + n);
	}
}
