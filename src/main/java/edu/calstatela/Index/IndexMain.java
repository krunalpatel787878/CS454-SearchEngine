package edu.calstatela.Index;

import java.io.IOException;

public class IndexMain {

	String indexDir = "C:/Users/Ami/CS454_workspace/CS454-SearchEngine/temp";
	   String dataDir = "C:/Users/Ami/CS454_workspace/CS454-SearchEngine/testing";
	   Indexer indexer;
	   
	   public static void main(String[] args) {
		   IndexMain tester;
	      try {
	         tester = new IndexMain();
	         tester.createIndex();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } 
	   }

	   private void createIndex() throws IOException{
	      indexer = new Indexer(indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");		
	   }

}
