package edu.calstatela.Index;

public class ShowSearchResultBean {
	public float score;
	public String link;
	public String desc;
	public String title;

	public ShowSearchResultBean(float score, String link, String desc, String title) {
		this.score = score;
		this.link = link;
		this.desc = desc;
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
