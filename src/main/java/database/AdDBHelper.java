package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import admanager.entity.Advertisement;


/**
 * @author dwt
 * 数据库广告检索工具类
 */
public class AdDBHelper {
	private static String banner_table = "rv_banners";
	private static String banner_key = "bannerid";
	private static int id_index = 1;
	private static String type_column = "contenttype";
	private static String keyword_column = "keyword";
	private static String code_column = "invocation_code";
	
	public static Advertisement queryById(String id){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		String sql = "select * from " + banner_table +" where creative_id=\""+id+"\"";
		ResultSet rs;
		Statement st;
		try {
			 st = conn.prepareStatement(sql);
			 rs = st.executeQuery(sql);
            while (rs.next()){
            	System.out.println(rs.getString(2));
            }
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Query advertisement info by list of ad ids.
	 * @param ids
	 * @return
	 */
	public static List<Advertisement> queryAdvertisementByIds(List<String> ids){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		ResultSet rs;
		Statement st;
		List<Advertisement> adList = new ArrayList<Advertisement>();	
		for(String id: ids){
//			String sql = "select * from " + banner_table +" where "+banner_key+"=\""+id+"\"";
			String sql = 
					"SELECT rv_banners.bannerid, rv_banners.contenttype, rv_banners.keyword,  rv_invocation_code.invocation_code "
					+ "FROM rv_banners "	
					+ "INNER JOIN rv_invocation_code "
					+ "ON rv_banners.bannerid = rv_invocation_code.bannerid "
					+ "WHERE rv_banners.bannerid=" + id+ " ";
			try {
				 st = conn.prepareStatement(sql);
				 rs = st.executeQuery(sql);
	            while (rs.next()){
	            	String type = rs.getString(type_column);
	            	type = !type.equals("txt") ? "img" : type;
	            	adList.add(new Advertisement(
	            			rs.getString(id_index), 
	            			type,
	            			Arrays.asList(rs.getString(keyword_column).split(",")),
	            			rs.getString(code_column))
	            	);
	            }
				 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return adList;
	}
	
	
	/**
	 * Query ad record for building index
	 */
	public static List<Advertisement> queryForIndex(){
		List<Advertisement> adList = new ArrayList<Advertisement>();
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		ResultSet rs;
		Statement st;
		String sql = 
				"SELECT rv_banners.bannerid, rv_banners.contenttype, rv_banners.keyword,  rv_invocation_code.invocation_code "
				+ "FROM rv_banners "	
				+ "INNER JOIN rv_invocation_code "
				+ "ON rv_banners.bannerid = rv_invocation_code.bannerid ";
		try {
			 st = conn.prepareStatement(sql);
			 rs = st.executeQuery(sql);
			 System.out.println(sql);
           while (rs.next()){
        	   //column从1开始计数 ，一个keyword一个实体
        	   for(String keyword: rs.getString(keyword_column).split(" ")){
        		   adList.add(new Advertisement(rs.getString(1), keyword, true));    
        	   }   	   
           }			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adList;
	}
	
	public static void main(String[] args){
		queryForIndex();
	}
}
