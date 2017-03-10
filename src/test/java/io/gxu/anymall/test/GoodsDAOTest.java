package io.gxu.anymall.test;

import io.gxu.anymall.dao.GoodsDAO;
import io.gxu.anymall.db.DBConnectionPool;
import io.gxu.anymall.model.Goods;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GoodsDAOTest {

	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	// 自动连接，使用unicode编码，设定时区
	private String dbUrl = "jdbc:mysql://localhost:3306/anymall?autoReconnect=true&useUnicode=true&serverTimezone=Asia/Shanghai";
	private String dbUser = "anymall_admin";
	private String dbPass = "llamyna";
	private DBConnectionPool pool;
	private GoodsDAO dao;

	@Before
	public void createPool() throws Exception {
		pool = new DBConnectionPool(jdbcDriver, dbUrl, dbUser, dbPass);
		dao = new GoodsDAO(pool);
	}

	@After
	public void destroy() {

	}

	@Test
	public void testFindGoodsById() {
		Goods goods = dao.findById(1);
		System.out.println("find user: " + goods);
	}

}
