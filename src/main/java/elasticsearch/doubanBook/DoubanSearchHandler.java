package elasticsearch.doubanBook;

import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;

import elasticsearch.doubanBook.DoubanBook;
import elasticsearch.doubanBook.SearchResult;
import elasticsearch.es.LRUCache;
import global.util.JsonUtil;

/**
 * @author dwt
 * 处理豆瓣读书本地检索
 */
public class DoubanSearchHandler {
	 private Client client;

	    public DoubanSearchHandler(){    
	        //使用本机做为节点
	        this("localhost");
	    }
	    
	    public DoubanSearchHandler(String host){
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
	        IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type).setRefresh(true);
	        for(int i=0; i<jsondata.size(); i++){
	            requestBuilder.setSource(jsondata.get(i)).execute().actionGet();
	        }     	         
	    }
	    
	    
	    
	    /**
	     * 执行豆瓣图书搜索
	     * @param queryBuilder
	     * @param indexname
	     * @param type
	     * @return
	     */
	    public SearchResult<DoubanBook> doubanSearcher(QueryBuilder queryBuilder, String indexname, String searchId,int start, int size){
	    	 SearchResult<DoubanBook> sr=null;
	    	 boolean cached = false;
	    	 List<DoubanBook> rst = new ArrayList<DoubanBook>();  
	         SearchResponse scrollResp = null;
	         if(!searchId.equals("-1")){
	        	 sr = LRUCache.getInstance().get(searchId);
	        	 if(sr!=null){
	        		 cached = true;
	        	 }   		 
	         }
	         if(!cached){
	        	 if(searchId.equals("-1"))
	        		 searchId = Long.toString(System.nanoTime());
		         	 scrollResp= client.prepareSearch(indexname)
//				           .addSort(SortParseElement.DOC_FIELD_NAME, SortOrder.ASC)
		         		   .setScroll(new TimeValue(60000))
			               .setQuery(queryBuilder)
			               .setSize(100)
			               .execute()
			               .actionGet(); 
		   	     System.out.println("record num: "+scrollResp.getHits().getTotalHits());
	         
		         
		       //Scroll until no hits are returned
		         while (true) {

		             for (SearchHit hit : scrollResp.getHits().getHits()) {
			        	String title = hit.getSource().get("keywords").toString().split(",")[0];
			        	DoubanBook db = new DoubanBook(hit.getSource().get("url").toString(),hit.getSource().get("info").toString(),title);
			        	rst.add(db);
		             }
		             scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		             //Break condition: No hits are returned
		             if (scrollResp.getHits().getHits().length == 0) {
		                 break;
		             }
		         }
		         
		         
		         sr = new SearchResult<DoubanBook>(rst,scrollResp.getHits().getTotalHits(),searchId);
		         LRUCache.getInstance().put(searchId, sr);
	         }           	
	    	 return sr;
	     }
	    
	    
	    public static void main(String[] args) {
	         String indexname = "douban";
	         String type = "html";
	         String windowsPath = "D:\\Dvlp_workspace\\git\\crawler\\douban";
	         //String linuxPath="/home/es/program/crawler/douban/";
	         DoubanSearchHandler esHandler = new DoubanSearchHandler();
	    	// DeleteIndexResponse deleteResponse = esHandler.client.admin().indices().delete(new DeleteIndexRequest("douban")).actionGet();
	         List<String> jsondata = JsonUtil.readJsonDir(windowsPath);
	         esHandler.createIndexResponse(indexname, type, jsondata);
	         
	         //esHandler.client.close();
	         
	    }
	    

	    
	    public void close(){
	    	this.client.close();
	    }
}
