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
public class AdHelper {
	private static String table = "creative";
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
	            			rs.getString(1), rs.getString(2),Arrays.asList(rs.getString(3).split(",")),rs.getString(4))
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
        	   for(String keyword: rs.getString(3).split(",")){
        		   adList.add(new Advertisement(rs.getString(1),keyword,true));    //column从1开始计数 ，一个keyword一个实体
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
