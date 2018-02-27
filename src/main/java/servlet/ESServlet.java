package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import elasticsearch.doubanBook.DoubanBook;
import elasticsearch.doubanBook.SearchResult;
import elasticsearch.es.ESHandler;

/**
 * Elasticsearch本地检索Servlet
 */

public class ESServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int pageSize = 10;
       

    public ESServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String q = request.getParameter("q");
		//System.out.println("start "+searchText);
		int start = Integer.parseInt(request.getParameter("start"));
		int size = pageSize;
		String sid = request.getParameter("sid");
		System.out.println(q+" "+start+" "+size);
		
		SearchResult<DoubanBook> sr = ESHandler.queryDoubanBook(q,sid,start,size);
		List<DoubanBook> rst =sr.subResults(start,size); 
		JSONObject r = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("results", JSON.toJSON(rst));
		responseData.put("estimatedResultSize", sr.getEstimatedResultSize());
		responseData.put("sid", sr.getSearchId());

		r.put("responseData", responseData);
		String jsonRst = r.toJSONString();
			
		response.setContentType("text/html;charset=utf-8");   //不写这个中文会有乱码
		response.getWriter().print(jsonRst);
		response.getWriter().flush();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
