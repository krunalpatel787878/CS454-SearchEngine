package edu.calstatela.Index;

import java.io.IOException;

public class IndexMain {

	
	   static Indexer indexer;
	   
	   public static void main(String[] args) {
		   IndexMain tester;
	      try {
	         tester = new IndexMain();
	         tester.createIndex();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } 
	   }

	   public static void createIndex() throws IOException{
	      indexer = new Indexer(IndexConstants.indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(IndexConstants.dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");		
	   }

}
