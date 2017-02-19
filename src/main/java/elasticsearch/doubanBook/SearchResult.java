package elasticsearch.doubanBook;

import java.util.List;

/**
 * @author dwt
 * 封装Elasticsearch搜索结果
 * @param <T>
 */
public class SearchResult<T> {
	private List<T> results;
	private long estimatedResultSize;
	private String searchId;
	
	public String getSearchId() {
		return searchId;
	}

	public SearchResult(List<T> results,long estimatedResultSize,String searchId){
		this.results = results;
		this.estimatedResultSize = estimatedResultSize;
		this.searchId = searchId;
	}

	public List<T> getAllResults() {
		return results;
	}
	
	public List<T> subResults(int start, int size){
		int lastIndex = (int) (start+size<estimatedResultSize-1? start+size:estimatedResultSize-1);
		if(start>=lastIndex)
			return null;
		return results.subList(start, lastIndex); 
	}
	
	
	
	public long getEstimatedResultSize() {
		return estimatedResultSize;
	}

}
