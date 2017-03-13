package io.gxu.anymall.controller;

import io.gxu.anymall.dao.GoodsDAO;
import io.gxu.anymall.db.PoolManager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/goods")
public class GoodsCtrl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private transient Logger log = Logger.getLogger(GoodsCtrl.class);
	private GoodsDAO goodsDAO;

	@Override
	public void init() throws ServletException {
		super.init();
		goodsDAO = new GoodsDAO(PoolManager.getInstance().getPool());
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();
		 
 		String rawContent = IOUtils.toString(request.getReader());
		log.debug(String.format("request content: %s", rawContent));
		
		JSONObject params = new JSONObject(rawContent);
		int id = params.getInt("id");
		System.out.println(id);
		out.print(goodsDAO.findById(id));
	}

}
