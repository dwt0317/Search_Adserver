package querymanager.rewriting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import admanager.entity.Keyword;

public class QueryRewriter {
	
	
	/**
	 * Rewrite query by word embedding
	 * @param q
	 * @return
	 */
	
	public static List<Keyword> broadRW(String q){
		List<Keyword> rwWordList = new ArrayList<Keyword>();
		String rewrtingRst = RewritingHandler.getInstance().rewriteQuery(q);
		if(rewrtingRst != null && !rewrtingRst.equals("")){
			String[] splits = rewrtingRst.split(" ");
			if(splits.length!=0){
				for(String str:splits){
					String[] keyword = str.split(",");
					rwWordList.add(new Keyword(keyword[0],Float.parseFloat(keyword[1])));
				}
			}
		}
		return rwWordList;
	}
}
