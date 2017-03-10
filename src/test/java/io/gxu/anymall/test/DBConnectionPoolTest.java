package io.gxu.anymall.test;

import io.gxu.anymall.db.DBConnectionPool;
import io.gxu.anymall.db.PooledConnection;

import java.sql.SQLException;
import java.util.List;

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
		List<Object[]> rs = conn.execQuery("select * from am_user_user");
		assert (rs != null);

		System.out.println("query result: ");

		for (Object[] row : rs) {
			for (int i = 0; i < row.length; i++) {
				System.out.print(row[i] + "\t");
			}
			System.out.println();
		}
	}

	@Test(timeout = 10000)
	public void testQueryWithParams() throws SQLException {
		Object[] params = new Object[] { 1 };
		List<Object[]> rs = conn.execQuery(
				"select * from am_user_user where gender=?", params);

		assert (rs != null);

		System.out.println("query with param result: ");
		for (Object[] row : rs) {
			for (int i = 0; i < row.length; i++) {
				System.out.print(row[i] + "\t");
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
