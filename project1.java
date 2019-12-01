import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import org.apache.log4j.PropertyConfigurator;



public class project1 {
       
       
       public static void CreateIndeex(String filepath,String filepath2) {
    	   
    	   Map<String, ArrayList<String>> map=new HashMap<>();// to create the map to store the key word and the filename
           ArrayList<String> list;//to store the filename
           Map<String,Integer> nums = new HashMap<>();//to create the nums to store the key words and the frequent of word 
           int i;
           String[] words =null;// to store the word
    	   
    	   try {
    		   File file = new File(filepath);//to read the file through the file path
    		   File allfile[] = file.listFiles(); //
    		   System.out.println(allfile.length);
    		   String line =System.getProperty("line.separator");//to get the next line
    		   StringBuffer str = new StringBuffer();
    		   FileWriter wf= new FileWriter(filepath2,true);//to write down the content 
    		      		   
    		   for( i=0 ;i<allfile.length;i++){	  			  
    			   String filename= allfile[i].getName();//  get the file name
    			   
    			   FileReader file2 =new FileReader(allfile[i]);//to create the filereader to read the file
    			   BufferedReader reader=new BufferedReader(file2);//to read the article
    			   
    			   String s =null;
    			  
    			   while((s=reader.readLine())!=null) { //to read article by line
    				   
    				     String txt = s.toString().replaceAll("[\\pP\\pS\\pN]"," ");// to remove the special character
    			         words=txt.split(" "); // split by space
    			         
    			         for(String string : words){
    	        			   if (!map.containsKey(string)) {//if map can't contain this word
    	        				   list = new ArrayList<String>();//to store the filename    				   
    	        				   list.add(filename);//put this file name into list
    	        				   map.put(string,list);//put <word,filename> into map
    	        				   nums.put(string,1);//to record the number of time a word appears 
    	        			   }else {   // if map contain this word    				   
    	        				   list=map.get(string);//to get the the list of this word
    	        				   if(!list.contains(filename)) {//if the list don't contain this filename
    	        					   list.add(filename);
    	        				   }//add the file name
    	        				   int count = nums.get(string)+1;//the number plus 1
    	        				   nums.put(string, count);//to update the nums
    	        				 
    	        			   }                  		   
    	            	   }    
    			         
    			         
        		   }  
    			   reader.close();    //to close the reader			     			   
    		   }   
    		  
    		   Set value =map.entrySet();//to get the all object   		   
			   Iterator ite=value.iterator();	
			   
			   while(ite.hasNext()) {//to check if there are element here
				   Map.Entry entry=(Map.Entry)ite.next();//to get the next element
				   str.append(entry.getKey()+":"+entry.getValue()).append(line);//make the word and filename into str by line
			   }
			   wf.write(str.toString());		   
			   wf.close();
    		  
   		     
    	   }catch (IOException e) {
    		   e.printStackTrace();
    		  
    	   }
    	   
       }
       
       public static void main(String[] args) throws IOException {
    	  long start= System.currentTimeMillis();
    	  String filepath="/home/uic/Desktop/material4";
    	  String filepath2="/home/uic/Desktop/index1";   	  
    	  CreateIndeex(filepath,filepath2) ;   	  
    	  long end = System.currentTimeMillis();
    	  System.out.println("success!!");
  		  System.out.println("the total time is:"+(end -start)+"ms");
    	     	   
       }
    	   
}
