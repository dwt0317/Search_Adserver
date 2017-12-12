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
        
        PriorityQueue<Advertisement> imgpq = new PriorityQueue<Advertisement>(1, rankCompare);
        PriorityQueue<Advertisement> txtpq = new PriorityQueue<Advertisement>(1, rankCompare);
        for (Advertisement ad: adList){
        	if (ad.getType() == "img")
        		imgpq.offer(ad);
        	else txtpq.offer(ad);
        }
        
		List<Advertisement> rankedAds = new ArrayList<Advertisement>();
		for (int i = 0; i < 1 && !imgpq.isEmpty(); i++){
			System.out.println(imgpq.peek().getId()+": "+imgpq.peek().getScore());
			rankedAds.add(imgpq.peek());
			imgpq.poll();
		}
		for (int i = 0; i < 1 && !txtpq.isEmpty(); i++){
			System.out.println(txtpq.peek().getId()+": "+txtpq.peek().getScore());
			rankedAds.add(txtpq.peek());
			txtpq.poll();
		}
		
		DeliverUtil.generateImpressionID(rankedAds);
		DeliverUtil.assignPosition(rankedAds);
		DeliverUtil.buildHTMLCode(rankedAds);
		return JsonUtil.adList2JsonRst(rankedAds, rewriteQ);
	}	

}
