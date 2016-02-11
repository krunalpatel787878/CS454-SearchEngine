package edu.calstatela.cs454;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;





public class SaveURL {

	public static void FileWriterToStorage(URL path) {
		try {
			URLConnection conn = null;
			try {
				conn = path.openConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block

			}
			InputStream input = null;
			try {
				input = path.openStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block

			}
			File dir = new File("Storage");
			if (!dir.exists()) {
				try {
					dir.mkdir();
				} catch (SecurityException se) {

				}
			}

			String w = conn.getContentType();
			String id = UUID.randomUUID().toString();
			if (w.contains("pdf") || w.contains("application/pdf")) {

				FileOutputStream fs = null;
				try {
					fs = new FileOutputStream("Storage/" + id+ ".pdf");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block

				}
				int len = -1;
				byte[] buffer = new byte[1024];
				try {
					while ((len = input.read(buffer)) > -1) {
						fs.write(buffer, 0, len);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

				}
			}  else if (w.contains("jpg") || w.contains("jpeg")) {

				FileOutputStream fs = null;
				try {
					fs = new FileOutputStream("Storage/"
							+id+ ".jpg");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block

				}
				int len = -1;
				byte[] buffer = new byte[1024];
				try {
					while ((len = input.read(buffer)) > -1) {
						fs.write(buffer, 0, len);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

				}
			}
			else  {

				FileOutputStream fs = null;
				try {
					fs = new FileOutputStream("Storage/"
							+id + ".html");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block

				}
				int len = -1;
				byte[] buffer = new byte[1024];
				try {
					while ((len = input.read(buffer)) > -1) {
						fs.write(buffer, 0, len);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

				}
			}

		} catch (NullPointerException e) {

		}

	}

	public static Vector extractLinks(String rawPage, String page) {
		int index = 0;
		Vector links = new Vector();
		while ((index = page.indexOf("<a ", index)) != -1) {
			if ((index = page.indexOf("href", index)) == -1)
				break;
			if ((index = page.indexOf("=", index)) == -1)
				break;
			String remaining = rawPage.substring(++index);
			StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\"'>#");
			String strLink = st.nextToken();
			if (!links.contains(strLink))
				links.add(strLink);
		}
		return links;
	}

	public static String getURL(URL url) throws IOException {
		StringWriter sw = new StringWriter();
		saveURL(url, sw);
		return sw.toString();
	}

	public static void saveURL(URL url, Writer writer) throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		for (int c = in.read(); c != -1; c = in.read()) {
			writer.write(c);
		}
	}

}
