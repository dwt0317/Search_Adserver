package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import admanager.retrieval.AdRetrieval;
import logger.CLogger;


/**
 * @author dwt
 * 广告召回servlet
 */
public class AdServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String q = request.getParameter("q");
		String adCall = request.getParameter("adCall");   //包含请求的广告信息， adCall的内容还未确定	
		
		//r: textAds:[], imgAds:[], rewriteQ: rewrited query
		JSONObject r = AdRetrieval.getAds(q, adCall);
		response.setContentType("text/html;charset=utf-8");   //不写这个中文会有乱码
		response.getWriter().print(r.toString());
//		response.getWriter().print("");  //for test purpose
		response.getWriter().flush();
		
		CLogger.impressionLog(request, r);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
