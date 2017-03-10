package io.gxu.anymall.dao;

import io.gxu.anymall.db.DBConnectionPool;
import io.gxu.anymall.db.PooledConnection;
import io.gxu.anymall.model.Goods;

import java.math.BigDecimal;
import java.util.List;

public class GoodsDAO extends AbstractDAO {

	public GoodsDAO(DBConnectionPool pool) {
		super(pool);
	}

	public Goods findById(int id) {
		PooledConnection conn = this.connectionFactory.getConnection();
		try {
			List<Object[]> dataset = conn
					.execQuery(
							"select goods_id, goods_title, goods_price,sort_order from am_core_goods where goods_id=?",
							new Object[] { id });
			if (dataset.size() > 1) {
				Object[] rawUser = dataset.get(1);
				Goods goods = new Goods();
				goods.setGoodsId((Integer) rawUser[0]);
				goods.setGoodsTitle((String) rawUser[1]);
				goods.setGoodsPrice(((BigDecimal) rawUser[2]).doubleValue());
				return goods;
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
