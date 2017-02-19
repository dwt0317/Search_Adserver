package admanager.index;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import admanager.entity.Advertisement;
import database.AdHelper;

/**
 * @author dwt
 * 构建广告检索索引工具类
 */
public class RetrievalIndexHelper {
	/**
	 * Read data for building index and convert them to JSON
	 */
	public static List<String> readIndexInfoAsList(){
		List<String> jsonData = new ArrayList<String>();
		List<Advertisement> adList = AdHelper.queryForIndex();
		for(Advertisement ad: adList){
			jsonData.add(JSON.toJSONString(ad));
		}
		System.out.println(jsonData.toString());
		return jsonData;
	}
}
