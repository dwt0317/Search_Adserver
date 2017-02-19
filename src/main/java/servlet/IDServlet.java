package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import usermanager.IDGenerator;

/**
 * @author dwt
 * ID生成servlet（用户cookieid生成）
 */
public class IDServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("start "+searchText);
		String id = IDGenerator.generate2JSON();			
		response.setContentType("text/html;charset=utf-8");   //不写这个中文会有乱码
		response.getWriter().print(id);
		response.getWriter().flush();
	
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
