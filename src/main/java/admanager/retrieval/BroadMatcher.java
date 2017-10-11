package admanager.retrieval;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import admanager.entity.Advertisement;
import admanager.entity.Keyword;
import database.AdDBHelper;
import elasticsearch.es.ESHandler;
import global.util.JsonUtil;
import global.util.DeliverUtil;
import global.util.WebUtil;
import querymanager.rewriting.QueryRewriter;

public class BroadMatcher {

	
	/**
	 * Retrieve ads with similar query found by word embedding (Broad match)
	 * There is not enough query entity to do query embedding so we use word instead.
	 * @param q
	 * @return
	 */
//	public static JSONObject getSimilarAds(String q){
//		List<Keyword> rewriteQ = QueryRewriter.broadRW(q);
//		List<Advertisement> adList = retrieveFromDB(rewriteQ);
//		DeliverUtil.generateImpressionID(adList);
//		DeliverUtil.assignPosition(adList);
//		DeliverUtil.buildHTMLCode(adList);
//		return JsonUtil.adList2JsonRst(adList, rewriteQ);
//	}
	
	public static List<Advertisement> getSimilarAds(String q){
		List<Keyword> rewriteQ = QueryRewriter.broadRW(q);
		List<Advertisement> adList = retrieveFromDB(rewriteQ);
		return adList;
	}
	
	
	public static List<Advertisement> retrieveFromDB(List<Keyword> rewriteQ){		
		List<String> adIDs = ESHandler.retrieveAdsByKeywords(rewriteQ);		
		return AdDBHelper.queryAdvertisementByIds(adIDs);
	}
	
}
