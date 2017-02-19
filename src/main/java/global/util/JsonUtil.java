package global.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import admanager.entity.Advertisement;
import admanager.entity.Keyword;

/**
 * @author dwt
 * Json工具类
 */

public class JsonUtil {
	
	/**
	 * 读取文件夹内的json文件
	 */
	public static List<String> readJsonDir(String dir) {
		List<String> jsonData = new ArrayList<String>();
		File file = new File(dir);
		if(!file.isDirectory())
			readJsonFile(dir);
		else{
			File[] files = file.listFiles();
			for(File f:files){
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(f));
					String line;
					while((line = br.readLine())!=null){
						jsonData.add(line);
					}
				} catch (Exception e) {
					System.err.println("open file error");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}			
		return jsonData;
	}
	
	public static List<String> readJsonFile(String file){
		return null;
	}
	
	/**
	 * Convert list of advertisement to JSON result which is to be returned to web page
	 */
	public static JSONObject adList2JsonRst(List<Advertisement> adList, List<Keyword> keywords){
		JSONObject jsonRst = new JSONObject();
		List<Advertisement> textAds = new ArrayList<Advertisement>();
		List<Advertisement> imgAds = new ArrayList<Advertisement>();
		for(Advertisement ad: adList){
			if(ad.getType().equals("img")) 
				imgAds.add(ad);			
			else
				textAds.add(ad);			
		}
		jsonRst.put("imgAds", JSON.toJSON(imgAds));
		jsonRst.put("textAds", JSON.toJSON(textAds));
		String rewriteRst="";
		for(Keyword tag: keywords){   //return rewrite keywords is for testing purpose
			rewriteRst+=tag.getWord()+":"+tag.getCount()+"\t";
		}
		jsonRst.put("rewriteQ", rewriteRst);
		return jsonRst;
	}
	
}
