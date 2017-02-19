package admanager.retrieval;

import com.alibaba.fastjson.JSONObject;


/**
 * @author dwt
 * 广告检索接口类
 */
public class AdRetrieval {
	public static JSONObject getAds(String q,String adCall){
		//now default is broad match	
		return BroadMatcher.getSimilarAds(q); 
	}	

}
