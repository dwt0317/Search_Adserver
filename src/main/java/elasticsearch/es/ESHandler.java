package elasticsearch.es;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import admanager.entity.Keyword;
import elasticsearch.ad.AdRetrievalHandler;
import elasticsearch.doubanBook.DoubanBook;
import elasticsearch.doubanBook.DoubanSearchHandler;
import elasticsearch.doubanBook.SearchResult;

/**
 * @author dwt
 * es检索请求处理（本地检索/广告检索）
 */
public class ESHandler {
	
    /**
     * Create es query for douban book search
     */
    public static SearchResult<DoubanBook> queryDoubanBook(String searchText, String searchId, int start , int size){
    	SearchResult<DoubanBook> sr;
    	DoubanSearchHandler esHandler = new DoubanSearchHandler();
        String indexname = "douban";
        String type = "html";
        QueryBuilder qb = QueryBuilders.commonTermsQuery("keywords", searchText);
        sr = esHandler.doubanSearcher(qb, indexname,searchId, start, size);
        esHandler.close();   
		return sr; 	
   }
   
    /**
     * Create es query for ad retrieval
     */
    public static List<String> retrieveAdsByKeywords(List<Keyword> keywords){
    	//to avoid dupilcates
    	Set<String> retrievalRst = new HashSet<String>();   
    	AdRetrievalHandler esHandler = new AdRetrievalHandler();
        String indexname = "creative";
        String type = "keywords";
        QueryBuilder queryBuilder;
        for(Keyword keyword: keywords){
        	System.out.println(keyword.getWord());
        	queryBuilder = QueryBuilders.termsQuery("keyword", keyword.getWord());
        	retrievalRst.addAll(esHandler.queryAdByKeywords(queryBuilder, indexname));
        }        
        esHandler.close();   
    	return new ArrayList(retrievalRst);
    }
}
