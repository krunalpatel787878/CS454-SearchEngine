package edu.calstatela.Index;

public class ShowSearchResultBean {
	public float score;
	public String link;

	public ShowSearchResultBean(float score, String link) {
		this.score = score;
		this.link = link;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
