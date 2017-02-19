package elasticsearch.es;

import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;

import elasticsearch.doubanBook.SearchResult;

import java.util.ArrayList;


/**
 * @author dwt
 * Elasticsearch本地检索时使用的LRUCache
 */
public class LRUCache<K,V> {
	
	private static final float   hashTableLoadFactor = 0.75f;
	
	private LinkedHashMap<K,V>   map;
	private static int defalutCacheSize = 10;
	private int cacheSize;
	public static LRUCache lru;
	
	private LRUCache (int cacheSize) {
		   this.cacheSize = cacheSize;
		   int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
		   map = new LinkedHashMap<K,V>(hashTableCapacity, hashTableLoadFactor, true) {
		      // (an anonymous inner class)
		      private static final long serialVersionUID = 1;
		      @Override protected boolean removeEldestEntry (Map.Entry<K,V> eldest) {
		         return size() > LRUCache.this.cacheSize; 
		         }
		      }; 
	}
	
	public static LRUCache<String,SearchResult> getInstance(){
		if(lru==null)
			lru = new LRUCache<String,SearchResult>(defalutCacheSize);
		return lru;
	}
	
	
	public synchronized V get (K key) {
	   return map.get(key); 
	}
	
	public synchronized void put (K key, V value) {
	   map.put (key, value); 
	}
	
	public synchronized void clear() {  
		   map.clear(); 
    }  
		  
	/** 
	* Returns the number of used entries in the cache. 
	* @return the number of entries currently in the cache. 
	*/  
	public synchronized int usedEntries() {  
	   return map.size(); 
	}  
	  
	/** 
	* Returns a <code>Collection</code> that contains a copy of all cache entries. 
	* @return a <code>Collection</code> with a copy of the cache content. 
	*/  
	public synchronized Collection<Map.Entry<K,V>> getAll() {  
	   return new ArrayList<Map.Entry<K,V>>(map.entrySet()); 
	}  
	

} // end class LRUCache