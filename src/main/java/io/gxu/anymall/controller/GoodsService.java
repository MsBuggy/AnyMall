package io.gxu.anymall.controller;

import io.gxu.anymall.annotation.Service;
import io.gxu.anymall.annotation.ServiceEntry;
import io.gxu.anymall.annotation.ServiceEntry.HttpMethod;
import io.gxu.anymall.dao.GoodsDAO;
import io.gxu.anymall.model.Goods;

@Service("/goods")
public class GoodsService {

	private GoodsDAO goodsDAO;

	public GoodsService(GoodsDAO dao) {
		this.goodsDAO = dao;
	}
	
	@ServiceEntry(value = "/get", type = HttpMethod.GET)
	public Goods findById(int id) {
		return goodsDAO.findById(id);
	}

}
