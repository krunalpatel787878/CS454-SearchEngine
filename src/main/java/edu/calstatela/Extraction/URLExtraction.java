package edu.calstatela.Extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

public class URLExtraction {
	public static void listFilesForFolder(final File folder) {
		String filename = "";
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            //System.out.println(fileEntry.getName());
	            filename = fileEntry.getName();
	            Extraction(filename);
	        }
	    }
	    //return filename;
	}
	
	@SuppressWarnings("unchecked")
	public static void Extraction(String fileName){
		 try {
	                 
	            String resourceLocation = "Storage/"+fileName;
	            
	            File file = new File(resourceLocation);
	              
	            InputStream input = new FileInputStream(file);           
	           // System.out.println( file.getPath());                
	             
	            Metadata metadata = new Metadata();
	             
	            BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
	            AutoDetectParser parser = new AutoDetectParser();       
	     
	            parser.parse(input, handler, metadata);
	           
	            JSONObject obj = new JSONObject(); 
	           
	            String[] metadataNames = metadata.names();
	             obj.put("storage", file.getPath());
	            // Display all metadata
	            for(String name : metadataNames){
	                //System.out.println(name + ": " + metadata.get(name));
	            	obj.put(name,metadata.get(name));
	            }
	             File f2 = new File("extraction.json");
	             BufferedWriter file2 = new BufferedWriter(new FileWriter(f2,true));
	             try{
	            	 ObjectMapper mapper = new ObjectMapper();
	            	 file2.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
	            	 
	            	 file2.newLine();
	            	 System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
	             
	             
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }finally{
	            	file2.flush();
	            	file2.close();
	            }
	              
		 }
		 catch(Exception e){}
	}
	
		
	public static void main(String[] args) {
		final File folder = new File("Storage");
		 listFilesForFolder(folder);
	//	 System.out.println(filename);
		 
	}
}
