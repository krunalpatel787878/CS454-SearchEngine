package edu.calstatela.cs454;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WebCrawlerThread extends ControllableThread {
	public void process(Object o) {

		try {
			URL path = (URL) o;
			
			String rawPage = SaveURL.getURL(path);

			String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

			Vector links = SaveURL.extractLinks(rawPage, smallPage);
			JSONObject jsonObj = new JSONObject();

			JSONArray jsonArray = new JSONArray();
			for (int n = 0; n < links.size(); n++) {
				// if (level != 0) {
				try {
					URL link = new URL(path, (String) links.elementAt(n));

					if (tc.getMaxLevel() == -1)
						queue.push(link, level);
					else
						queue.push(link, level + 1);

				} catch (MalformedURLException e) {
				}

			}
		} catch (IOException e) {

		}

	}

}
