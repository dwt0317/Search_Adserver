package admanager.retrieval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.alibaba.fastjson.JSONObject;

import admanager.entity.Advertisement;
import admanager.entity.Keyword;
import admanager.ranking.AdsRanker;
import global.util.JsonUtil;
import global.util.DeliverUtil;
import querymanager.rewriting.QueryRewriter;


/**
 * @author dwt
 * 广告检索接口类
 */
public class AdRetrieval {
	public static JSONObject getAds(String q,String adCall){
		//now default is broad match	
//		List<Advertisement> adList = BroadMatcher.getSimilarAds(q);
		List<Keyword> rewriteQ = QueryRewriter.broadRW(q);
		List<Advertisement> adList = BroadMatcher.retrieveFromDB(rewriteQ);
		
		//ranking ads
		AdsRanker.rankingAds(adList, adCall);
        Comparator<Advertisement> rankCompare =  new Comparator<Advertisement>(){  
            public int compare(Advertisement o1, Advertisement o2) {  
                // TODO Auto-generated method stub  
                double numbera = o1.getScore();  
                double numberb = o2.getScore();  
                if(numberb > numbera){  
                    return 1;  
                }  
                else if(numberb < numbera){  
                    return -1;  
                }  
                else {  
                    return 0;  
                }  
            }  
        };
        
        PriorityQueue<Advertisement> pq = new PriorityQueue<Advertisement>(3, rankCompare);
        for (Advertisement ad: adList){
        	pq.offer(ad);
        }
        
		List<Advertisement> rankedAds = new ArrayList<Advertisement>();
		for (int i = 0; i < 3 && !pq.isEmpty(); i++){
			System.out.println(pq.peek().getId()+": "+pq.peek().getScore());
			rankedAds.add(pq.peek());
			pq.poll();
		}
		DeliverUtil.generateImpressionID(rankedAds);
		DeliverUtil.assignPosition(rankedAds);
		DeliverUtil.buildHTMLCode(rankedAds);
		return JsonUtil.adList2JsonRst(rankedAds, rewriteQ);
	}	

}
