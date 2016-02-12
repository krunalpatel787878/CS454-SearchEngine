package edu.calstatela.cs454;

public class WebCrawler implements MessageReceiver {
	public WebCrawler(Queue q, int maxLevel, int maxThreads)
			throws InstantiationException, IllegalAccessException {
			ThreadController tc = new ThreadController(WebCrawlerThread.class,
													   maxThreads,
													   maxLevel,
													   q,
													   0,
													   this);
		}
	
	public static void main(String[] args){
		
	}
}
