package edu.calstatela.Index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	   Search searchLink;


	   private void search(String searchQuery) throws IOException, ParseException, SAXException, TikaException{
		   searchLink = new Search(IndexConstants.indexDir);
		      long startTime = System.currentTimeMillis();
		      TopDocs hits = searchLink.search(searchQuery);
		      long endTime = System.currentTimeMillis();

		      System.out.println(hits.totalHits +
		         " documents found. Time :" + (endTime - startTime) +" ms");
		      for(ScoreDoc scoreDoc : hits.scoreDocs) {
		         Document doc = searchLink.getDocument(scoreDoc);
		         File file = new File(doc.get(IndexConstants.FILE_PATH));
	              
		         InputStream input = new FileInputStream(file);           
		           // System.out.println( file.getPath());                
		             
		            Metadata metadata = new Metadata();
		             
		            BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
		            AutoDetectParser parser = new AutoDetectParser();       
		     
		            parser.parse(input, handler, metadata);
		         
		         
		        
		            if(metadata.get("og:url")!=null){
		            System.out.print("Score: "+ scoreDoc.score + " ");
		         System.out.println("File: "+ metadata.get("og:url"));
		            }
		         
		       /*  System.out.print("Score: "+ scoreDoc.score + " ");
		         System.out.println("File: "+ doc.get(IndexConstants.FILE_PATH));
		         */
		      }
		      searchLink.close();
		   }	
	   @SuppressWarnings("unused")
	private void sortUsingRelevance(String searchQuery) throws IOException, ParseException, SAXException, TikaException{
		   searchLink = new Search(IndexConstants.indexDir);
	      long startTime = System.currentTimeMillis();
	      //create a term to search file name
	      Term term = new Term(IndexConstants.CONTENTS, searchQuery);
	      //create the term query object
	      Query query = new FuzzyQuery(term);
	      searchLink.setSortScoring(true, false);
	      //do the search
	      TopDocs hits = searchLink.search(query,Sort.RELEVANCE);
	      long endTime = System.currentTimeMillis();

	      System.out.println(hits.totalHits +
	         " documents found. Time :" + (endTime - startTime) + "ms");
	      for(ScoreDoc scoreDoc : hits.scoreDocs) {
	         Document doc = searchLink.getDocument(scoreDoc);
	         
	       /*  File file = new File(doc.get(IndexConstants.FILE_PATH));
          
	         InputStream input = new FileInputStream(file);           
	           // System.out.println( file.getPath());                
	             
	            Metadata metadata = new Metadata();
	             
	            BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
	            AutoDetectParser parser = new AutoDetectParser();       
	     
	            parser.parse(input, handler, metadata);
	         
	         
	        
	            if(metadata.get("og:url")!=null){
	            System.out.print("Score: "+ scoreDoc.score + " ");
	         System.out.println("File: "+ metadata.get("og:url"));
	            }
	         */
	         
	         
	         System.out.print("Score: "+ scoreDoc.score + " ");
	         System.out.println("File: "+ doc.get(IndexConstants.FILE_PATH));
	      }
	      searchLink.close();
	   }

	   @SuppressWarnings("unused")
	private void sortUsingIndex(String searchQuery)
	      throws IOException, ParseException{
		   searchLink = new Search(IndexConstants.indexDir);
	      long startTime = System.currentTimeMillis();
	      //create a term to search file name
	      Term term = new Term(IndexConstants.CONTENTS, searchQuery);
	      //create the term query object
	      Query query = new FuzzyQuery(term);
	      searchLink.setSortScoring(true, false);
	      //do the search
	      TopDocs hits = searchLink.search(query,Sort.INDEXORDER);
	      long endTime = System.currentTimeMillis();

	      System.out.println(hits.totalHits +
	      " documents found. Time :" + (endTime - startTime) + "ms");
	      for(ScoreDoc scoreDoc : hits.scoreDocs) {
	         Document doc = searchLink.getDocument(scoreDoc);
	         System.out.print("Score: "+ scoreDoc.score + " ");
	         System.out.println("File: "+ doc.get(IndexConstants.FILE_PATH));
	      }
	      searchLink.close();
	   }
	
	
	
}
