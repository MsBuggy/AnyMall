package io.gxu.anymall.db;

import java.util.HashMap;

public class PoolManager {

	private HashMap<String, DBConnectionPool> pools;
	private DBConnectionPool defaultPool;
	private static PoolManager INSTANCE;

	private PoolManager() {
		this.pools = new HashMap<String, DBConnectionPool>();
	}

	public static PoolManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PoolManager();
		}
		return INSTANCE;
	}

	public DBConnectionPool createPool(String id, String driver, String url,
			String user, String pass) {
		try {
			DBConnectionPool pool = new DBConnectionPool(driver, url, user,
					pass);
			INSTANCE.pools.put(id, pool);
			if (INSTANCE.defaultPool == null) {
				INSTANCE.defaultPool = pool;
			}
			return pool;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public DBConnectionPool getPool(String id) {
		return INSTANCE.pools.get(id);
	}

	public DBConnectionPool getPool() {
		return INSTANCE.defaultPool;
	}

	public int poolSize() {
		return INSTANCE.pools.size();
	}
}
