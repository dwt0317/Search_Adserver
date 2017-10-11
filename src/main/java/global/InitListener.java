package global;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import admanager.ranking.AdsRankingHandler;
import database.DBPool;
import logger.CLogger;
import querymanager.rewriting.RewritingHandler;

/**
 * @author dwt
 * Init the project
 * 初始化
 */
public class InitListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		RewritingHandler.getInstance().close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		initThriftService();
		initLogger();
	}
	
	//初始化thrift服务（连接的服务包括查询改写,广告排序）
	private void initThriftService(){
		Properties prop = new Properties();	
		InputStream thriftIs = getClass().getClassLoader().getResourceAsStream("properties/thrift.properties");				
		try {
			prop.load(thriftIs);	
//			Config.rewriting_port=Integer.parseInt(prop.getProperty("rewriting_port"));
//			Config.rewriting_url=prop.getProperty("rewriting_url");
			int rw_port = Integer.parseInt(prop.getProperty("rewriting_port"));
			String rw_url = prop.getProperty("rewriting_url");
			RewritingHandler.getInstance().init(rw_url, rw_port);
			
			int rk_port = Integer.parseInt(prop.getProperty("ranking_port"));
			String rk_url = prop.getProperty("ranking_url");
			AdsRankingHandler.getInstance().init(rk_url, rk_port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Failed to read thrift properties!");
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * 初始化数据库配置
	 */
	@Deprecated
	private void initDBService(){
		Properties prop = new Properties();	
		InputStream dbIs = getClass().getClassLoader().getResourceAsStream("properties/ad_db.properties");				
		try {
			prop.load(dbIs);	
		    String url = prop.getProperty("url");
		    String username = prop.getProperty("username");
		    String password = prop.getProperty("password");
		    int initPoolSize = Integer.parseInt(prop.getProperty("initPoolSize"));
		    int maxPoolSize = Integer.parseInt(prop.getProperty("maxPoolSize"));
		    int waitTime = Integer.parseInt(prop.getProperty("waitTime"));
		    DBPool.getInstance().init(url, username, password, initPoolSize, maxPoolSize, waitTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Failed to read ad_db properties!");
			e.printStackTrace();
		}	
	}
	
	/**
	 * 初始化日志收集
	 */
	private void initLogger(){	
		//init log4j configuration
	    Properties resource = new Properties();
	    InputStream in = getClass().getClassLoader().getResourceAsStream("properties/log4j.properties");
	    try {
			resource.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PropertyConfigurator.configure(resource);
	}
	
}
