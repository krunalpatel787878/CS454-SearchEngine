package edu.calstatela.Index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.xml.sax.ContentHandler;



public class Index {
public static void index(String c, String stw, String index, String rank) throws IOException{
		

			ArrayList<String> ranking = new ArrayList<String>();
			final InputStream in = new FileInputStream(c);
			 
			try {
				for (Iterator it = new ObjectMapper().readValues(
						new JsonFactory().createJsonParser(in), Map.class); it
						.hasNext();) {
					// System.out.println(it.next());
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> keyValue = (LinkedHashMap<String,String>) it.next();
					//System.out.println(keyValue.get("path"));
					//System.out.println(keyValue.get("title"));
		
		    	try{
		    		
		    		 InputStream input = new FileInputStream(keyValue.get("storage"));
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
		    	        	
		    	        	if(!ranking.contains(str))
		    	        	{
		    	        	ranking.add(str);
		    	            }
		    	        	
		    	        	ArrayList<String> word = new ArrayList<String>();
		    	        	FileInputStream fstream = new FileInputStream(stw);
		    	        	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		    	        	String strLine;

		    	        	//Read File Line By Line
		    	        	while ((strLine = br.readLine()) != null)   {
		    	        	  // Print the content on the console
		    	        		word.add(strLine);
		    	        	//  System.out.println (strLine);
		    	        	}

		    	        	//Close the input stream
		    	        	br.close();
	                         
		    	        	if(!word.contains(str))
		    	        	{
		    	        		
		    	        	String sr = str.toLowerCase();	
		    	            obj.put("word", sr);
		    	            obj.put("frequency", Collections.frequency(wordList, str));
		    	            obj.put("link",keyValue.get("storage"));
		    	            obj.put("title",keyValue.get("title"));
		    	            obj.put("url",keyValue.get("og:url"));
		    	        	}
		    	        
		    	        File f2 = new File(index);
				          BufferedWriter file2 = new BufferedWriter(new FileWriter(f2,true)); 
				            try {
				            	
				            	ObjectMapper mapper = new ObjectMapper();
				               file2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
				          System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
				                file2.newLine();
				                file2.newLine();
				                file2.newLine();
				                file2.newLine();
				               
				     
				            } catch (IOException e) {
				                e.printStackTrace();
				     
				            } finally {
				                file2.flush();
				                file2.close();
				            }
		    	        }

		    	}
		    	catch(FileNotFoundException e){
					
				}
				catch(Exception e){
					
				}
		    }
				
			}
			finally {
				
				in.close();
			}
	}

	public static void main(String[] args) throws IOException{
		
		Index.index("C:/Users/Ami/CS454_workspace/CS454-SearchEngine/extraction.json", "C:/Users/Ami/CS454_workspace/CS454-SearchEngine/words.txt", "C:/Users/Ami/CS454_workspace/CS454-SearchEngine/index.json", "1");
	}
}
