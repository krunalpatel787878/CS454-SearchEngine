package edu.calstatela.Index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;




public class Search {
	IndexSearcher indexSearcher;
	   QueryParser queryParser;
	   Query query;

	  
	   public Search(String indexDirectoryPath) throws IOException{
	      Directory indexDirectory 
	         = FSDirectory.open(new File(indexDirectoryPath));
	      indexSearcher = new IndexSearcher(indexDirectory);
	      queryParser = new QueryParser(Version.LUCENE_CURRENT,
	         IndexConstants.CONTENTS,
	         new StandardAnalyzer(Version.LUCENE_CURRENT));
	   }

	   public TopDocs search( String searchQuery) 
	      throws IOException, ParseException{
	      query = queryParser.parse(searchQuery);
	      return indexSearcher.search(query, IndexConstants.MAX_SEARCH);
	   }

	   public TopDocs search(Query query) 
	      throws IOException, ParseException{
		 
	      return indexSearcher.search(query, IndexConstants.MAX_SEARCH);
	   }


	   public TopFieldDocs search(Query query,Sort sort) 
			      throws IOException, ParseException{
		   
				   return   indexSearcher.search(query, null,  IndexConstants.MAX_SEARCH, sort);
							 
			   }
	  
	   public void setSortScoring(boolean doTrackScores, 
	      boolean doMaxScores){
	      indexSearcher.setDefaultFieldSortScoring(
	         doTrackScores,doMaxScores);
	   }

	   public Document getDocument(ScoreDoc scoreDoc) 
	      throws CorruptIndexException, IOException{
	      return indexSearcher.doc(scoreDoc.doc);	
	   }

	   public void close() throws IOException{
	      indexSearcher.close();
	   }
}
