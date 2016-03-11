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


/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		List<String> c = new ArrayList<String>();
		String file = "D:\\CS454_Workspace_Github\\CS454-SearchEngine\\MyFile.txt";
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
			//r = SearchMain.sortUsingRelevance(query);
		} else {
			System.out.println(token[0] + "" + token[1]);
			if (operators.equals("AND") || operators.equals("and") || operators.equals("And")) {
				//	r = SearchMain.searchByBoolean(token[0].trim(),token[1].trim(),"1");
			} else {
				//r = SearchMain.searchByBoolean(token[0].trim(), token[1].trim(), "0");
			}
		}
		List<String> c = new ArrayList<String>();
		String file = "D:\\CS454_Workspace_Github\\CS454-SearchEngine\\MyFile.txt";
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
