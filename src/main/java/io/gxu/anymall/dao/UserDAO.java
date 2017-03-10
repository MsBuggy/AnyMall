package io.gxu.anymall.dao;

import io.gxu.anymall.db.DBConnectionPool;
import io.gxu.anymall.db.PooledConnection;
import io.gxu.anymall.model.User;

import java.sql.Date;
import java.util.List;

public class UserDAO extends AbstractDAO {

	public UserDAO(DBConnectionPool pool) {
		super(pool);
	}

	public User findById(int id) {
		PooledConnection conn = this.connectionFactory.getConnection();
		try {
			List<Object[]> dataset = conn
					.execQuery(
							"select id,name,gender,date_of_birth from am_user_user where id=?",
							new Object[] { id });
			if (dataset.size() > 1) {
				Object[] rawUser = dataset.get(1);
				User user = new User();
				user.setId((Long) rawUser[0]);
				user.setName((String) rawUser[1]);
				user.setGender((Long) rawUser[2]);
				user.setDateOfBirth((Date) rawUser[3]);

				return user;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
}
