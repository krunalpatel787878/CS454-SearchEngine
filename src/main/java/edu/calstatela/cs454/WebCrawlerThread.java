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
			HashMap<Integer, String> web = new HashMap<Integer, String>();

			// URL url = new URL(path);

//			SaveURL.FileWriterToStorage(path);

			String rawPage = SaveURL.getURL(path);

			String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

			Vector links = SaveURL.extractLinks(rawPage, smallPage);
			JSONObject jsonObj = new JSONObject();

			JSONArray jsonArray = new JSONArray();
			for (int n = 0; n < links.size(); n++) {
				//if (level != 0) {
					try {
						URL link = new URL(path, (String) links.elementAt(n));

/*						jsonObj.put("URL", link);
						jsonArray.add(jsonObj);

						File f = new File("Storage/file1.json");

						BufferedWriter file = new BufferedWriter(
								new FileWriter(f, true));
						try {
							ObjectMapper mapper = new ObjectMapper();
							file.write(mapper.writerWithDefaultPrettyPrinter()
									.writeValueAsString(jsonObj));
							// System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));

							file.newLine();
							file.newLine();
						} catch (IOException e) {
							e.printStackTrace();

						} finally {
							file.flush();
							file.close();
						}
*/
						//System.out.println(link);
						if (tc.getMaxLevel() == -1)
							queue.push(link, level);
						else
							queue.push(link, level + 1);
						//SaveURL.LinkExtract(link, level - 1);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				//}

			}

			//return links;

		} catch (IOException e) {
			
		}

	}

}
