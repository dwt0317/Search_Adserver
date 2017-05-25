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
	private static String table = "creative";
	private static int id_index = 1;
	private static int type_index = 2;
	private static int keyword_index = 3;
	private static int url_index = 4;
	
	public static Advertisement queryById(String id){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		String sql = "select * from " + table +" where creative_id=\""+id+"\"";
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
	
	
	public static List<Advertisement> queryByIds(List<String> ids){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		ResultSet rs;
		Statement st;
		List<Advertisement> adList = new ArrayList<Advertisement>();	
		for(String id: ids){
			String sql = "select * from " + table +" where creative_id=\""+id+"\"";
			try {
				 st = conn.prepareStatement(sql);
				 rs = st.executeQuery(sql);
	            while (rs.next()){
	            	adList.add(new Advertisement(
	            			rs.getString(id_index), 
	            			rs.getString(type_index),
	            			Arrays.asList(rs.getString(keyword_index).split(",")),
	            			rs.getString(url_index))
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
		String sql = "SELECT * FROM creative";
		try {
			 st = conn.prepareStatement(sql);
			 rs = st.executeQuery(sql);
			 System.out.println(sql);
           while (rs.next()){
        	 //column从1开始计数 ，一个keyword一个实体
        	   for(String keyword: rs.getString(keyword_index).split(",")){
        		   adList.add(new Advertisement(rs.getString(1), keyword, true));    
        	   }   	   
           }			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return adList;
	}
	
	public static void main(String[] args){
		queryForIndex();
	}
}
