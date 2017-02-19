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
		List<Keyword> rewriteQ = new ArrayList<Keyword>();
		List<String> qwords = Arrays.asList(WordSegment.getInstance().querySegment(q));
		//被切分后的原关键词设置评分为1.0
		for(String qword:qwords){
			rewriteQ.add(new Keyword(qword, 1.0f));	
		}
		String simiRst = Similar.getInstance().similarWords(qwords);
		if(simiRst != null && !simiRst.equals("")){
			String[] splits = simiRst.split(",");
			if(splits.length!=0){
				for(String str:splits){
					String[] keyword = str.split(":");
					rewriteQ.add(new Keyword(keyword[0],Float.parseFloat(keyword[1])));
				}
			}
		}
		return rewriteQ;
	}
}
