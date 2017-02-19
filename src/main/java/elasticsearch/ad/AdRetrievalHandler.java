package elasticsearch.ad;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;

import admanager.entity.Keyword;
import admanager.index.RetrievalIndexHelper;
import admanager.retrieval.BroadMatcher;
import elasticsearch.doubanBook.DoubanBook;
import elasticsearch.doubanBook.SearchResult;
import elasticsearch.es.ESHandler;
import global.util.JsonUtil;

/**
 * @author dwt
 * 广告检索模块
 */
public class AdRetrievalHandler {
	 private Client client;
	    public AdRetrievalHandler(){    
	        //使用本机做为节点
	        this("localhost");
	    }
	    
	    public AdRetrievalHandler(String host){
	        //集群连接超时设置
	        try {
	        	client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
			} catch (UnknownHostException e) {
				System.err.println("connect cluser error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    
	    /**
	     * 建立索引,索引建立好之后,会在elasticsearch-0.20.6\data\elasticsearch\nodes\0 创建索引
	     * @param indexName  为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
	     * @param indexType  Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
	     * @param jsondata     json格式的数据集合
	     * @return
	     */
	    public void createIndexResponse(String indexname, String type, List<String> jsondata){
	        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
	        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexname);
	        String mapping_json = readMappingFile();
	        createIndexRequestBuilder.addMapping("keywords", mapping_json).get();
	        IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type).setRefresh(true);
	        for(int i=0; i<jsondata.size(); i++){
	        	requestBuilder.setSource(jsondata.get(i)).execute().actionGet();
	        }    
	    }
	    
	    /**
	     * 创建索引
	     * @param client
	     * @param jsondata
	     * @return
	     */
	    public void createIndexResponse(String indexname, String type,String jsondata){
	    	 List<String> jsonList = new ArrayList<String>(1);
	    	 jsonList.add(jsondata);
	    	 createIndexResponse(indexname, type, jsonList);
	    }
	    
	    
	    /**
	     * 执行广告检索，返回 list of creative_id
	     */
	    public List<String> queryAdByKeywords(QueryBuilder queryBuilder, String indexname){
	    	 List<String> retrievalRst = new ArrayList<String>();
	         SearchResponse scrollResp = null;
         	 scrollResp= client.prepareSearch(indexname)
         		   .setScroll(new TimeValue(60000))
	               .setQuery(queryBuilder)
	               .execute()
	               .actionGet(); 
	   	     System.out.println("record num: "+scrollResp.getHits().getTotalHits());
//         	 System.out.println(scrollResp.getHits().);
         	         
	       //Scroll until no hits are returned
	         while (true) {
	             for (SearchHit hit : scrollResp.getHits().getHits()) {
		        	String id = hit.getSource().get("id").toString();   //es会自动将*_id转化为id
		        	retrievalRst.add(id);
	             }
	             scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
	             //Break condition: No hits are returned
	             if (scrollResp.getHits().getHits().length == 0) {
	                 break;
	             }
	         }    	
	    	 return retrievalRst;
	     }
	    
	    
		private String readMappingFile(){
			String mapping_json = "";
			InputStream input = getClass().getResourceAsStream("/es/mappings");
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line;
			try {
				while((line=br.readLine())!=null){
					mapping_json += line + System.lineSeparator();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return mapping_json;
		}
	    
	    public static void main(String[] args) {
	         String indexname = "creative";
	         String type = "keywords";
	         AdRetrievalHandler esHandler = new AdRetrievalHandler();
	         DeleteIndexResponse deleteResponse = esHandler.client.admin().indices().delete(new DeleteIndexRequest("creative")).actionGet();
	         esHandler.createIndexResponse(indexname, type, RetrievalIndexHelper.readIndexInfoAsList());
	         esHandler.close();
	         String[] ks = {"耳机","戒指","耳","指","耳麦"};
	         List<Keyword> kl = new ArrayList<Keyword>();
	         for(String s: ks){
	        	 kl.add(new Keyword(s,1.0f));
	         }
	         JsonUtil.adList2JsonRst(BroadMatcher.retrieveFromDB(kl), kl);
	    }
	    

	    
	    public void close(){
	    	this.client.close();
	    }
}
