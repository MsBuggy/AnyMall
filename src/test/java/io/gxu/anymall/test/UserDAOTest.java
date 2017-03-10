package io.gxu.anymall.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.gxu.anymall.dao.UserDAO;
import io.gxu.anymall.db.DBConnectionPool;
import io.gxu.anymall.db.PooledConnection;
import io.gxu.anymall.model.User;

public class UserDAOTest {

	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	// 自动连接，使用unicode编码，设定时区
	private String dbUrl = "jdbc:mysql://localhost:3306/anymall?autoReconnect=true&useUnicode=true&serverTimezone=Asia/Shanghai";
	private String dbUser = "anymall_admin";
	private String dbPass = "llamyna";
	private DBConnectionPool pool;
	private UserDAO dao;

	@Before
	public void createPool() throws Exception {
		pool = new DBConnectionPool(jdbcDriver, dbUrl, dbUser, dbPass);
		dao = new UserDAO(pool);
	}

	@After
	public void destroy() {

	}

	@Test
	public void testFindUserById() {
		User user = dao.findById(1);
		System.out.println("find user: " + user);
	}

}
