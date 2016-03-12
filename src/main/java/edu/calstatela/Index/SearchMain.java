package edu.calstatela.Index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;



public class SearchMain {
	
	
	   Indexer indexer;
	   static Search searchLink;

	 /*  public static void main(String[] args) throws SAXException, TikaException {
		      SearchMain searchData;
		      try {
		    	  searchData = new SearchMain();
		          //tester.searchUsingBooleanQuery("students","computer");
		    	  SearchMain.search("mayor");
		       // tester.sortUsingRelevance("body");
		          Index_tfidf.readDocs("D:\\CS454_Workspace_Github\\CS454-SearchEngine\\Storage");
		          Index_tfidf.computeTF2();
		          //tester.sortUsingIndex("03ab818d-62bf-4c42-95ba-9bfc1e141043.html");
		      } catch (IOException e) {
		          e.printStackTrace();
		      } catch (ParseException e) {
		          e.printStackTrace();
		      }		
		   }
	   */
		@SuppressWarnings("unused")
		public static List<ShowSearchResultBean> searchByBoolean(String searchQuery1,
				String searchQuery2,String value) throws IOException,
				ParseException, SAXException, TikaException {
			List<ShowSearchResultBean> result = new ArrayList<>();

			searchLink = new Search(IndexConstants.indexDir);
			long startTime = System.currentTimeMillis();
			// create a term to search file name
			Term term1 = new Term(IndexConstants.CONTENTS, searchQuery1);
			// create the term query object
			Query query1 = new TermQuery(term1);

			Term term2 = new Term(IndexConstants.CONTENTS, searchQuery2);
			// create the term query object
			Query query2 = new PrefixQuery(term2);

			BooleanQuery query = new BooleanQuery();
			if (value == "1") {
				query.add(query1, BooleanClause.Occur.MUST);
				query.add(query2, BooleanClause.Occur.MUST);
			} else {
				query.add(query1, BooleanClause.Occur.SHOULD);
				query.add(query2, BooleanClause.Occur.SHOULD);
			}
			
			// do the search
			TopDocs hits = searchLink.search(query);
			long endTime = System.currentTimeMillis();

			System.out.println(hits.totalHits + " documents found. Time :"
					+ (endTime - startTime) + "ms");
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searchLink.getDocument(scoreDoc);

				//

				File file = new File(doc.get(IndexConstants.FILE_PATH));

				InputStream input = new FileInputStream(file);
				// System.out.println( file.getPath());

				Metadata metadata = new Metadata();

				BodyContentHandler handler = new BodyContentHandler(
						10 * 1024 * 1024);
				AutoDetectParser parser = new AutoDetectParser();

				parser.parse(input, handler, metadata);

				//
				if (metadata.get("og:url") != null) {
					/*
					 * System.out.print("Score: "+ scoreDoc.score + " ");
					 * System.out.println("File: "+ metadata.get("og:url"));
					 */
					ShowSearchResultBean a = new ShowSearchResultBean(
							scoreDoc.score, metadata.get("og:url"), metadata.get("description"), metadata.get("title"));
					result.add(a);

				}
			}

			searchLink.close();
			return result;
		}

		static List<ShowSearchResultBean> search(String searchQuery)
				throws IOException, ParseException, SAXException, TikaException {
			List<ShowSearchResultBean> result = new ArrayList<>();
			searchLink = new Search(IndexConstants.indexDir);
			long startTime = System.currentTimeMillis();
			TopDocs hits = searchLink.search(searchQuery);
			long endTime = System.currentTimeMillis();

			System.out.println(hits.totalHits + " documents found. Time :"
					+ (endTime - startTime) + " ms");
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searchLink.getDocument(scoreDoc);
				File file = new File(doc.get(IndexConstants.FILE_PATH));

				InputStream input = new FileInputStream(file);
				// System.out.println( file.getPath());

				Metadata metadata = new Metadata();

				BodyContentHandler handler = new BodyContentHandler(
						10 * 1024 * 1024);
				AutoDetectParser parser = new AutoDetectParser();

				parser.parse(input, handler, metadata);

				if (metadata.get("og:url") != null) {
					/*
					 * System.out.print("Score: "+ scoreDoc.score + " ");
					 * System.out.println("File: "+ metadata.get("og:url"));
					 */
					ShowSearchResultBean a = new ShowSearchResultBean(
							scoreDoc.score, metadata.get("og:url"), metadata.get("description"), metadata.get("title"));
					result.add(a);
				}

				/*
				 * System.out.print("Score: "+ scoreDoc.score + " ");
				 * System.out.println("File: "+ doc.get(IndexConstants.FILE_PATH));
				 */
			}
			searchLink.close();
			return result;
		}

		@SuppressWarnings("unused")
		public static List<ShowSearchResultBean> sortUsingRelevance(String searchQuery) throws IOException,
				ParseException, SAXException, TikaException {
			List<ShowSearchResultBean> result = new ArrayList<>();
			searchLink = new Search(IndexConstants.indexDir);
			long startTime = System.currentTimeMillis();
			// create a term to search file name
			Term term = new Term(IndexConstants.CONTENTS, searchQuery);
			// create the term query object
			Query query = new FuzzyQuery(term);
			searchLink.setSortScoring(true, false);
			// do the search
			TopDocs hits = searchLink.search(query, Sort.RELEVANCE);
			long endTime = System.currentTimeMillis();

			System.out.println(hits.totalHits + " documents found. Time :"
					+ (endTime - startTime) + "ms");
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searchLink.getDocument(scoreDoc);

				
				  File file = new File(doc.get(IndexConstants.FILE_PATH));
				  
				  InputStream input = new FileInputStream(file); //
				 System.out.println( file.getPath());
			 
				  Metadata metadata = new Metadata();
				  
				  BodyContentHandler handler = new
				  BodyContentHandler(10*1024*1024); AutoDetectParser parser = new
				  AutoDetectParser();
				  
				  parser.parse(input, handler, metadata);
				  
				  
				  
				  if(metadata.get("og:url")!=null){ 
					  /*System.out.print("Score: "+
				  scoreDoc.score + " "); System.out.println("File: "+
				  metadata.get("og:url"));*/
					  ShowSearchResultBean a = new ShowSearchResultBean(
								scoreDoc.score, metadata.get("og:url"), metadata.get("description"), metadata.get("title"));
						result.add(a);  
				  }
				 

				//System.out.print("Score: " + scoreDoc.score + " ");
				//System.out.println("File: " + doc.get(IndexConstants.FILE_PATH));
			}
			searchLink.close();
			return result;
		}

		@SuppressWarnings("unused")
		private void sortUsingIndex(String searchQuery) throws IOException,
				ParseException {
			searchLink = new Search(IndexConstants.indexDir);
			long startTime = System.currentTimeMillis();
			// create a term to search file name
			Term term = new Term(IndexConstants.CONTENTS, searchQuery);
			// create the term query object
			Query query = new FuzzyQuery(term);
			searchLink.setSortScoring(true, false);
			// do the search
			TopDocs hits = searchLink.search(query, Sort.INDEXORDER);
			long endTime = System.currentTimeMillis();

			System.out.println(hits.totalHits + " documents found. Time :"
					+ (endTime - startTime) + "ms");
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document doc = searchLink.getDocument(scoreDoc);
				System.out.print("Score: " + scoreDoc.score + " ");
				System.out.println("File: " + doc.get(IndexConstants.FILE_PATH));
			}
			searchLink.close();
		}
}
