package elasticsearch.doubanBook;

/**
 * @author dwt
 * class of DoubanBook object when searching data crawled from book.douban.com
 */
public class DoubanBook {
	private String url;
	private String info;
	private String title;
	
	public DoubanBook(String url,String info,String title){
		this.url = url;
		this.info = info;
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
