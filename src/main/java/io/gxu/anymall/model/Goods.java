package io.gxu.anymall.model;

import org.json.JSONObject;

public class Goods {
	private String goodsTitle;
	private String goodsSubtitle;
	private String unit;
	private int goodsId;
	private double goodsPrice;
	private int categoryId;

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsSubtitle() {
		return goodsSubtitle;
	}

	public void setGoodsSubtitle(String goodsSubtitle) {
		this.goodsSubtitle = goodsSubtitle;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public String toString() {
 		return new JSONObject(this).toString();
	}
}
