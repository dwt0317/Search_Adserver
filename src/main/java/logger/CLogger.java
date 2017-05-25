package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import admanager.entity.Advertisement;
import global.Config;
import global.util.WebUtil;

/**
 * @author dwt
 * 日志采集
 */
public class CLogger {

	//t=time, ck=userid, ip=ip, q=q
	public static void searchLog(HttpServletRequest request, String seid){
		String log = commonLog(request);
	    String q = request.getParameter("q");
	    log += "q="+q;
//	    System.out.println(Config.search_log);
	    Logger search = Logger.getLogger("search");		//直接流入hdfs
	    search.info(log);  
	}
	
	//t=time, ck=userid, ip=ip, se=searchid, im=impressionid, ad=adid, po=position
	public static void impressionLog(HttpServletRequest request, JSONObject r){
		String log = commonLog(request);
		log += request.getParameter("seid");	//搜索id
	    Logger impression = Logger.getLogger("impression");		//直接流入hdfs
	    
		JSONArray textAds = r.getJSONArray("textAds");
		JSONArray imgAds = r.getJSONArray("imgAds");
		for (int i = 0; i < textAds.size(); i++){
			String ilog = log;
			JSONObject j = textAds.getJSONObject(i);
			ilog += "im=" + j.getString("impressionID") + "&" + "ad=" + j.getString("id") + "&" + "po=" + j.getString("position");
			impression.info(ilog); 
		}	
		for (int i = 0; i < imgAds.size(); i++){
			String ilog = log;
			JSONObject j = imgAds.getJSONObject(i);
			ilog += "im=" + j.getString("impressionID") + "&" + "ad=" + j.getString("id") + "&" + "po=" + j.getString("position");
			impression.info(ilog); 
		}	
	}
	
	//t=time, ck=userid, ip=ip, se=searchid, im=impressionid, ad=adid, po=position, url=url
	public static void clickLog(HttpServletRequest request){
		String log = commonLog(request);
		String ad = request.getParameter("click[ad]");	//use html5 data-click attribute
		String im = request.getParameter("click[im]");
		String po = request.getParameter("click[po]");
		String url = request.getParameter("url");
		log += "im=" + im + "&" + "ad=" + ad + "&" + "po=" + po + "&" + "url=" + url;
	    Logger click = Logger.getLogger("click");		//直接流入hdfs
	    click.info(log);  
	}
	
	
	/**
	 * Get common part of three kinds of log, time, userid and ip
	 */
	private static String commonLog(HttpServletRequest request){
		String time = String.valueOf(System.currentTimeMillis()); 
		String userid = "";
		Cookie[] cookies = request.getCookies();
	    if (cookies != null ){
	    	for (Cookie cookie: cookies){
	    		if (cookie.getName().equals("userid")){
	    			userid = cookie.getValue();
	    			break;
	    		}		
	    	}
		}		
	    else System.err.println("cookies not found");
	    String ip = WebUtil.getIp(request);
	    String log = "t="+time+"&" + "ck="+userid+"&" + "ip="+ip+"&";
	    return log;
	}
}
