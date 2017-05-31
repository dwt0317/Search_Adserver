package admanager.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dwt
 * Advertisement entity
 */
public class Advertisement {
	private String type;  //0-9: text ad   10- :img ad
	private List<String> keywords;
	private String keyword;
	private String code;
	private String id;
	private String url;

	private String impressionID; //impression id, temporary place
	private String position;  //ad position
	
	private double score;
	
	//true means ad is initialized for retrieval indexing
	public Advertisement(String id, String keyword, boolean index){  
		this.keyword = keyword;
		this.id = id;
	}
	
	
	public Advertisement(String id, String type, List<String> keywords, String url){
		this.type = type;
		this.keywords = keywords;
		this.url = url;
		this.id = id;
	}
	
	public Advertisement(String id, List<String> keywords){
		this.id = id;
		this.keywords = keywords;
	}
	
	public Advertisement(String type,  String code){
		this.code = code;
		this.keywords = new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setTag(ArrayList<String> tag) {
		this.keywords = tag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public void addKeyword(String newkeyword){
		this.keywords.add(newkeyword);
	}
	
	
	public String getKeyword() {
		return keyword;
	}


	@Override
	public String toString(){
		return "id:"+this.id+" type:"+this.type+" keywords:"+this.keywords.toString();
	}


	public String getImpressionID() {
		return impressionID;
	}


	public void setImpressionID(String impressionID) {
		this.impressionID = impressionID;
	}
	
	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}

	
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public double getScore() {
		return score;
	}


	public void setScore(double score) {
		this.score = score;
	}
}
