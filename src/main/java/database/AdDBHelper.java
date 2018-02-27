package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import admanager.entity.Advertisement;
import database.FieldName;

/**
 * @author dwt
 * 数据库广告检索工具类
 */
/**
 * @author dwt
 *
 */
/**
 * @author dwt
 *
 */
public class AdDBHelper {

	
	public static Advertisement queryById(String id){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		String sql = "select * from " + FieldName.banner_table +" where creative_id=\""+id+"\"";
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
	            	String type = rs.getString(FieldName.type_column);
	            	type = (!type.equals("txt")) ? "img" : type;
	            	adList.add(new Advertisement(
	            			rs.getString(FieldName.id_index), 
	            			type,
	            			Arrays.asList(rs.getString(FieldName.keyword_column).split(",")),
	            			rs.getString(FieldName.code_column))
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
        	   for(String keyword: rs.getString(FieldName.keyword_column).split(" ")){
        		   adList.add(new Advertisement(rs.getString(1), keyword, true));    
        	   }   	   
           }			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adList;
	}
	
	
	/**
	 * Query if client has enough budget to display ads
	 */
	public static void queryClientInfo(Advertisement ad){
		String bannerid = ad.getId();
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		String sql = "SELECT rv_campaigns.campaignid, rv_campaigns.clientid, rv_campaigns.revenue, rv_clients.balance " 
				+ "FROM rv_banners "	
				+ "INNER JOIN rv_campaigns "
				+ "ON rv_banners.campaignid = rv_campaigns.campaignid "
				+ "INNER JOIN rv_clients "
				+ "ON rv_campaigns.clientid = rv_clients.clientid "
				+ "WHERE bannerid=\""+bannerid+"\"";
		ResultSet rs;
		Statement st;

		try {
			 st = conn.prepareStatement(sql);
			 rs = st.executeQuery(sql);
            while (rs.next()){
            	ad.setPrice(rs.getDouble(FieldName.price_column));
            	ad.setBalance(rs.getDouble(FieldName.balance_column));
            	ad.setCampaignid(rs.getString(FieldName.campaignid_column));
            	ad.setClientid(rs.getString(FieldName.clientid_column));
            	return;
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//对客户进行扣费
	public static boolean charge(Advertisement ad){
		DBPool pool = DBPool.getInstance();
		Connection conn = pool.getTestConnection();
		ResultSet rs;
		Statement st;
		String sql = 
				"UPDATE rv_clients SET rv_clients.balance=rv_clients.balance - "
				+ ad.getPrice()	
				+ " WHERE rv_clients.clientid = "
				+ ad.getClientid();
		try {
			 st = conn.prepareStatement(sql);
			 rs = st.executeQuery(sql);
			 System.out.println(sql);		 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static void main(String[] args){
		Advertisement ad = new Advertisement("5", 5);
		charge(ad);
		
	}
}
