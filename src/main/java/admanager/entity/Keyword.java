package admanager.entity;

/**
 * @author dwt
 * Purchased keyword of advertising
 */
public class Keyword implements Comparable{
	private String word;   //word entity
	private float score;   //priority
	private String type;   //not used
	
	public Keyword(String word,float score){
		this.word=word;
		this.score=score;
	}

	public String getWord() {
		return word;
	}

	public float getCount() {
		return score;
	}

	public String getType() {
		return type;
	}

	@Override
	public int compareTo(Object o) {
		Keyword t = (Keyword)o;
		if(this.score<t.score)
			return -1;
		if(this.score>t.score)
			return 1;
		return 0;
	}
	
}
