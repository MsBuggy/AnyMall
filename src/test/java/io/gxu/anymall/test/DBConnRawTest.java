package io.gxu.anymall.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

public class DBConnRawTest {

	@Test
	public void testCreation() throws Exception {

		String dbUrl = "jdbc:mysql://localhost:3306/anymall?autoReconnect=true&useUnicode=true&serverTimezone=Asia/Shanghai";
		String dbUser = "anymall_admin";
		String dbPass = "llamyna";
		Driver driver = (Driver) Class.forName("com.mysql.jdbc.Driver")
				.newInstance();
		DriverManager.registerDriver(driver);

		Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select goods_id as id,goods_title as title from am_core_goods");
	
		if(rs!=null){//iteration
			while(rs.next()){
				int id = rs.getInt("id");
				String title = rs.getString("title");
				System.out.println(String.format("%s\t%s", id,title));
			}
		}
		
		conn.close();
	}

}
