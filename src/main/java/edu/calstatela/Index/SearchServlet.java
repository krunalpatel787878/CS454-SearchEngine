package edu.calstatela.Index;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;


public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public SearchServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		/*IndexMain im = new IndexMain();
		im.createIndex();
		
		Index_tfidf i_tfidf = new Index_tfidf();
		i_tfidf.readDocs(IndexConstants.dataDir);    	
		try {
			i_tfidf.computeTF2();
		} catch (SAXException e1) {
			
			e1.printStackTrace();
		} catch (TikaException e1) {
			
			e1.printStackTrace();
		}
		*/
		Ranking r = new Ranking();
		try {
			r.startRank("C:\\Users\\Ami\\CS454_workspace\\CS454-SearchEngine\\Control.json");
		} catch (org.json.simple.parser.ParseException e1) {

			e1.printStackTrace();
		}
		List<String> c = new ArrayList<String>();
		String file = "C:\\Users\\Ami\\CS454_workspace\\CS454-SearchEngine\\MyFile.txt";
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		String text = null;
		int j = 0;
		while (true) {

			try {
				text = buf.readLine();
				if (!c.contains(text)) {
					c.add(text);
				}
			} catch (IOException e) {

			}
			if (text == null) {
				break;
			}
			j++;
		}

		request.setAttribute("AutoList", c);
		request.getRequestDispatcher("/Search.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
String q = request.getParameter("search");
		
		String query = q.toLowerCase();

		String operators = "";
		//String text = "Los angeles or el monte";

		List<String> tokens = new ArrayList<String>();
		tokens.add("or");
		tokens.add("and");
		tokens.add("AND");
		tokens.add("OR");
		tokens.add("And");
		tokens.add("Or");

		String patternString = "\\b(" + StringUtils.join(tokens, "|") + ")\\b";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(query);
		String[] token = null;
		while (matcher.find()) {
			// System.out.println(matcher.group(1));
			token = query.split(matcher.group(1));
			operators = matcher.group(1);
		}

		List<ShowSearchResultBean> r = null;
		if (token == null) {
			System.out.println(query);
			try {
				r = SearchMain.sortUsingRelevance(query);
			} catch (ParseException e) {
				
				e.printStackTrace();
			} catch (SAXException e) {
				
				e.printStackTrace();
			} catch (TikaException e) {
				
				e.printStackTrace();
			}
		} else {
			System.out.println(token[0] + "" + token[1]);
			if (operators.equals("AND") || operators.equals("and") || operators.equals("And")) {
				try {
					r = SearchMain.searchByBoolean(token[0].trim(),token[1].trim(),"1");
				} catch (ParseException e) {
					
					e.printStackTrace();
				} catch (SAXException e) {
				
					e.printStackTrace();
				} catch (TikaException e) {
					
					e.printStackTrace();
				}
			} else {
				try {
					r = SearchMain.searchByBoolean(token[0].trim(), token[1].trim(), "0");
				} catch (ParseException e) {
					
					e.printStackTrace();
				} catch (SAXException e) {
					
					e.printStackTrace();
				} catch (TikaException e) {
				
					e.printStackTrace();
				}
			}
		}
		List<String> c = new ArrayList<String>();
		String file = "C:\\Users\\Ami\\CS454_workspace\\CS454-SearchEngine\\MyFile.txt";
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		String text = null;
		int j = 0;
		while (true) {

			try {
				text = buf.readLine();
				if (!c.contains(text)) {
					c.add(text);
				}
			} catch (IOException e) {

			}
			if (text == null) {
				break;
			}
			j++;
		}

		request.setAttribute("AutoList", c);

		request.setAttribute("result", r);
		request.getRequestDispatcher("/Search.jsp").forward(request, response);
	}

}
