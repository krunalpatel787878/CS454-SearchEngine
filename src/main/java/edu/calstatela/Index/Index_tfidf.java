package edu.calstatela.Index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Index_tfidf {
	public static HashMap<String, Double> TermFrequencyWholeCorpus = new HashMap<String, Double>();
	public static HashMap<String, HashMap<Integer, Double>> TermFrequencyDocWise = new HashMap<String, HashMap<Integer, Double>>();
	public static Vector<String> docs=new Vector<String>();
	public static Vector<String> docsWithUrl=new Vector<String>();
	public static Vector<String> termsList = new Vector<String>();

	public static double[][] matrix;

	public static double docCount=0;
	
	
	public static String readOneFile(File f){
		StringBuffer page=new StringBuffer();
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		/*String line ="";
		while((line=br.readLine())!=null){
		page.append(line);
		page.append(" ");*/
		 InputStream input = new FileInputStream(f);
		  ContentHandler handler = new BodyContentHandler();
	        Metadata metadata = new Metadata();
	        new HtmlParser().parse(input, handler, metadata, new ParseContext());
	        String plainText = handler.toString();
	       // System.out.println(plainText);
	        JSONObject obj = new JSONObject();
	        ArrayList<String> meta = new ArrayList<String>();
	       
	        
	       for(int i = 0; i <metadata.names().length; i++) { 
	        	  String name = metadata.names()[i]; 
	        	  meta.add(metadata.get(name));
	        	  }
	       String listString = "";

	       for (String s : meta)
	       {
	           listString += s + "\t";
	       }
         
	       String indexing = listString+plainText;
	       String result = indexing.replaceAll("[,]","");
	      // System.out.println(indexing);
	        
	        String[] stringArray = result.split("\\s+");
	        List<String> wordList = Arrays.asList(stringArray);
	     
	        
	        for (String str : stringArray)
	        {
	           // System.out.println(str);
	        	
	        	
	        	
	        	
	        	
	        	ArrayList<String> word = new ArrayList<String>();
	        	
	        	
	        		
	        	String sr = str.toLowerCase();	
	           /* obj.put("word", sr);
	            obj.put("frequency", Collections.frequency(wordList, str));
	            obj.put("link",keyValue.get("storage"));
	            obj.put("title",keyValue.get("title"));
	            obj.put("url",keyValue.get("og:url"));*/
	        	page.append(sr);
	    		page.append(" ");
		
		
		}

		}catch(Exception e){
		e.printStackTrace();
		}
		return page.toString();
		}

		public static void readDocs(String dir){
		try{

		File f=new File(dir);
		File files[]=f.listFiles();
		docCount = files.length;
		System.out.println("Reading "+files.length+" files");
		String line="";
		for(File filePath: files){
		String fileContent = readOneFile(filePath);
		docs.add(fileContent);
		docsWithUrl.add(filePath.getName());
		}

		}catch(Exception e){
		e.printStackTrace();
		}
		}
		
		
		public static String[] tokenize(String text){
			String terms[]=text.split("[^a-zA-Z0-9]+");
			return terms;
			}

			public static void computeFrequency(){
			for(int i=0;i<docs.size();i++){
			String page=docs.get(i).toLowerCase();

			String terms[]=tokenize(page);

			for(int j=0;j<terms.length;j++){
			if(terms[j].isEmpty())continue;

			double val=1;
			if(TermFrequencyWholeCorpus.get(terms[j])!=null)
			val = val+TermFrequencyWholeCorpus.get(terms[j]);
			TermFrequencyWholeCorpus.put(terms[j], val);

			double val2=1;
			HashMap<Integer, Double> tempInner = new HashMap<Integer, Double>();
			if(TermFrequencyDocWise.get(terms[j])!=null)
			tempInner = TermFrequencyDocWise.get(terms[j]);

			if(tempInner.get(i)!=null)
			val2 = val2+tempInner.get(i);

			tempInner.put(i, val2);
			TermFrequencyDocWise.put(terms[j], tempInner);
			}
			}
			termsList.addAll(TermFrequencyWholeCorpus.keySet());
			System.out.println(TermFrequencyWholeCorpus);
			}
			
			  public static void computeMatrix(){
				  matrix = new double[docs.size()][termsList.size()];

				  for(int i=0;i<docs.size();i++){
				  String page=docs.get(i).toLowerCase();
				  String terms[]=tokenize(page);

				  double maxTfThisDoc=-1.0;
				  for(int j=0;j<terms.length;j++){
				  if(terms[j].isEmpty())continue;

				  int ti = termsList.indexOf(terms[j]);
				  matrix[i][ti]++;

				  if(matrix[i][ti]>=maxTfThisDoc)
				  maxTfThisDoc = matrix[i][ti];
				  }

				  ////////////////////////Normalizing TF////////////////////////////
				  for(int j=0;j<terms.length;j++){
				  if(terms[j].isEmpty())continue;

				  int ti = termsList.indexOf(terms[j]);
				  matrix[i][ti] = matrix[i][ti]/(maxTfThisDoc*terms.length);

				  /////////////////////////TFIDF////////////////////////////////
				  double numDocsWithTerm= (double)TermFrequencyDocWise.get(terms[j]).size();
				  double DF = (double)docs.size() / numDocsWithTerm;

				  matrix[i][ti] = matrix[i][ti] * Math.log(1 + DF);
				  ///////////////////////////////////////////////////////
				  }
				  //////////////////////////////////////////////////////
				  }

				  }
			  
			  
			  public static void printMatrix(){
				  for(int j=0;j<termsList.size();j++){
				  System.out.print(" "+termsList.get(j));
				  }
				  System.out.println();
				  for(int i=0;i<docs.size();i++){
				  System.out.print(i);
				  for(int j=0;j<termsList.size();j++){
				  System.out.print(" "+matrix[i][j]);
				  }
				  System.out.println();
				  }
				  }
			  
			  
			  public static double[] queryVector=null;

			  public static HashMap<Integer, Double> docScores = new HashMap<Integer, Double>();
			  public static HashMap<Integer, HashMap<String, Double>> DocWiseTermFrequency = new HashMap<Integer, HashMap<String, Double>>();

			  @SuppressWarnings("unchecked")
			public static void computeTF2() throws IOException, SAXException, TikaException{

			  for(int i=0;i<docs.size();i++){
			  String page=docs.get(i).toLowerCase();
			  String terms[]=page.split("[^a-zA-Z0-9]+");
			  for(int j=0;j<terms.length;j++){
			  if(terms[j].isEmpty())continue;

			  double val=1;
			  if(TermFrequencyWholeCorpus.get(terms[j])!=null)
			  val = val+TermFrequencyWholeCorpus.get(terms[j]);
			  TermFrequencyWholeCorpus.put(terms[j], val);

			  double val2=1;
			  HashMap<Integer, Double> tempInner = new HashMap<Integer, Double>();
			  if(TermFrequencyDocWise.get(terms[j])!=null)
			  tempInner = TermFrequencyDocWise.get(terms[j]);

			  if(tempInner.get(i)!=null)
			  val2 = val2+tempInner.get(i);

			  tempInner.put(i, val2);
			  TermFrequencyDocWise.put(terms[j], tempInner);

			  HashMap<String, Double> termInner = new HashMap<String, Double>();
			  if(DocWiseTermFrequency.get(i)!=null)
			  termInner = DocWiseTermFrequency.get(i);
			  termInner.put(terms[j], val2);
			  DocWiseTermFrequency.put(i, termInner);

			  }
			  }
			  JSONObject obj = new JSONObject();
			  Iterator it =DocWiseTermFrequency.entrySet().iterator();
			
			  
			  File f2 = new File("f1.json");
	          BufferedWriter file2 = null;
			try {
				file2 = new BufferedWriter(new FileWriter(f2,true));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	            try {
	            	  while (it.hasNext()) {

	    				  Map.Entry m = (Map.Entry)it.next();
	            		  
	            		  
	            		  BodyContentHandler handler = new BodyContentHandler();
	        		      Metadata metadata = new Metadata();
	        		      FileInputStream inputstream = new FileInputStream( "C:/Users/Ami/CS454_workspace/CS454-SearchEngine/testing/"+docsWithUrl.get((int)m.getKey()));
	        		      ParseContext pcontext = new ParseContext();
	        		      AutoDetectParser  msofficeparser = new AutoDetectParser(); 
	        		      msofficeparser.parse(inputstream, handler, metadata,pcontext);
	            		  
	            		  
	    				  obj.put("a", metadata.get("og:url"));
	    				  obj.put("link", docsWithUrl.get((int)m.getKey()));
	    				  obj.put("words", m.getValue());
	    			// System.out.println(m.getKey()+"-"+m.getValue());
	    			  
	            	ObjectMapper mapper = new ObjectMapper();
	               file2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
	          System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
	                file2.newLine();
	            	  }
	            } catch (IOException e) {
	                e.printStackTrace();
	     
	            } finally {
	                file2.flush();
	                file2.close();
	            }
			  
			  
			 /* System.out.println(TermFrequencyWholeCorpus);
			  System.out.println(TermFrequencyDocWise);
			  System.out.println(DocWiseTermFrequency);*/
			  }
			  
			    public static void main(String[] args) throws IOException, SAXException, TikaException{
			    	readDocs("C:/Users/Ami/CS454_workspace/CS454-SearchEngine/testing");
			    	//computeFrequency();
			    	/*computeMatrix();
			    	printMatrix();*/
			    	computeTF2();
			    }
	

}
