package io.gxu.anymall.dao;

import io.gxu.anymall.db.DBConnectionPool;

public abstract class AbstractDAO {
	protected DBConnectionPool connectionFactory;

	public AbstractDAO(DBConnectionPool pool) {
		this.connectionFactory = pool;
	}
}
