package edu.calstatela.cs454;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
	public void finishedAll() {
		// ignore
	}

	public void receiveMessage(Object o, int threadId) {
		// In our case, the object is already string, but that doesn't matter
		URL path = (URL) o;		
		SaveURL.FileWriterToStorage(path);		
		System.out.println("[" + threadId + "] " + o.toString());
	}

	public void finished(int threadId) {
		System.out.println("[" + threadId + "] finished");
	}


	public static void main(String[] args) {
		
		Options options = new Options();
		options.addOption("d", true, "depth");
		options.addOption("u", true, "url");
		options.addOption("t", true, "thread");
		CommandLine cl = null;
		 
		BasicParser parser = new BasicParser();
		try {
			 cl = parser.parse(options, args);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//String d = System.getProperty("d");
		String pt = cl.getOptionValue("u"); /*"http://www.apple.com/"*/
		URL url = null;
		try {
			url = new URL(pt);
		} catch (MalformedURLException e) {
			
		}
		URLQueue q = new URLQueue();
		try {
			q.push(new URL(pt), 0);
		} catch (MalformedURLException e1) {			
		}
		try {
			//new WebCrawler(q, 1, 10);
			new WebCrawler(q, Integer.parseInt(cl.getOptionValue("d")), Integer.parseInt(cl.getOptionValue("t")));
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
		return;
	}
}
