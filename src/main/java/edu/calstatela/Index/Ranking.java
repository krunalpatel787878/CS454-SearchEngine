package edu.calstatela.Index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Ranking {

	private int numOfLinks;

	private List<ShowDataBean> allLinks = new ArrayList<ShowDataBean>();
	private HashMap<String, Integer> incomingCount = new HashMap<String, Integer>();
	private HashMap<String, Integer> outgoingCount = new HashMap<String, Integer>();
	private HashMap<String, ShowDataBean> records = new HashMap<String, ShowDataBean>();
	private JSONArray mainArr = new JSONArray();
	private JSONObject jsonObj = new JSONObject();

	public void startRank(String filePath) throws FileNotFoundException,
			IOException, ParseException {
		File jsonFile = new File(filePath);

		JSONParser jsonParser = new JSONParser();

		JSONArray jsonArr = (JSONArray) jsonParser.parse(new FileReader(
				jsonFile));

		numOfLinks = jsonArr.size();

		JSONObject jsonObject;
		ShowDataBean link;
		String path;
		String temp[];
		String url;
		JSONObject links;
		List<String> outGoing;
		String linkHolder;

		for (Object obj : jsonArr) {
			jsonObject = (JSONObject) obj;
			link = new ShowDataBean();
			path = (String) jsonObject.get("path");

			temp = path.split("\\\\");
			link.setId(temp[temp.length - 2]);

			url = (String) jsonObject.get("URL");
			links = (JSONObject) jsonObject.get("links");
			outGoing = new ArrayList<String>();

			for (Object l : links.keySet()) {
				linkHolder = (String) links.get(l);
				if (!linkHolder.equals(""))
					outGoing.add(linkHolder);

			}

			link.setPath(path);
			link.setUrl(url);
			link.setGoingOut(outGoing);

			allLinks.add(link);
			records.put(link.getUrl(), link);
		}

		int tempStore;

		for (ShowDataBean single : allLinks) {

			for (String eachUrl : single.getGoingOut()) {
				if (incomingCount.containsKey(eachUrl)) {
					tempStore = incomingCount.get(eachUrl);
					incomingCount.put(eachUrl, ++tempStore);
				} else
					incomingCount.put(eachUrl, 1);

				single.getIncoming().add(eachUrl);
			}
		}

		for (String single : incomingCount.keySet()) {
			if (records.containsKey(single)) {
				records.get(single).setPointedBy(incomingCount.get(single));
			}
		}

		Ranking();

	}

	public void Ranking() throws IOException {

		double defaultRank = 1.0 / numOfLinks;

		for (ShowDataBean link : allLinks) {
			link.setRank(defaultRank);
			link.setNewRank(defaultRank);
		}

		double rank;
		double tempRank;
		ShowDataBean holder;
		for (int i = 0; i < 10; i++) {

			for (String url : records.keySet()) {
				holder = records.get(url);
				rank = 0;
				for (String goingOut : holder.getGoingOut()) {
					if (records.containsKey(goingOut)) {
						tempRank = records.get(goingOut).getRank();
						if (incomingCount.containsKey(goingOut))
							if (incomingCount.get(goingOut) > 0)
								tempRank = tempRank
										/ incomingCount.get(goingOut);
						rank = rank + tempRank;
					} else {
						rank = rank + defaultRank;
					}
				}
				if (rank == 0.0)
					rank = defaultRank;

				holder.setNewRank(rank);
				holder.setFinalRank1();
			}
			for (ShowDataBean link : allLinks) {
				link.copyRank();
			}
		}

		for (ShowDataBean link : allLinks) {
			link.getFinalRank1();
		}

	

	}

}
