package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bing.BingSearch;
import elasticsearch.doubanBook.DoubanBook;
import elasticsearch.doubanBook.SearchResult;
import elasticsearch.es.ESHandler;
import global.util.WebUtil;
import logger.CLogger;
import usermanager.IDGenerator;

/**
 * @author dwt
 * Bing检索servlet
 */
public class BingServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String q = request.getParameter("q");
		String offset = request.getParameter("offset");
		String count = request.getParameter("count");
		System.out.println(q+" "+offset+" "+count);	
		String result = BingSearch.excuteBingSearch(q, count, offset);
		
		JSONObject jo = JSONObject.parseObject(result);
		String seid = "s" + IDGenerator.generate();
		jo.put("seid", seid);
		
		response.setContentType("text/html;charset=utf-8");   //不写这个中文会有乱码
		response.getWriter().print(jo.toJSONString());
		response.getWriter().flush();
		
		CLogger.searchLog(request, seid);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
