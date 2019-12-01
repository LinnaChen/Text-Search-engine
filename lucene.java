import java.io.IOException; 
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.fs.Path; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job; 
import org.apache.hadoop.mapreduce.Mapper; 
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.input.FileSplit; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.PropertyConfigurator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;


public class lucene {
	
	public static class indexmap extends Mapper<Object, Text, Text, Text> {
    	
    	private FileSplit name;
    	String filepath = "/home/uic/Desktop/index"; //build a file path to store the index
      
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
       
        	Analyzer analyzer = new StandardAnalyzer();// to create a new analysis to analysis the word
            Directory directory = FSDirectory.open(Paths.get(filepath));//to open the path 
            IndexWriterConfig config = new IndexWriterConfig(analyzer);//to build the path
            IndexWriter indexWriter = new IndexWriter(directory, config);//
            
            
            name = (FileSplit) context.getInputSplit();//to get the filepath through the split 
            String filename = name.getPath().getName().toString();// to get the filename during the filepath
            String word=value.toString();//to get the keyword 
          
            Document entry = new Document();//to create a new document to make the word and filename combine
            
            entry.add(new TextField("keyword", word, Field.Store.YES)); //to add the word 
            entry.add(new TextField("filename", filename, Field.Store.YES)); //to add the filename
            
            
            indexWriter.addDocument(entry);// write down the input
            indexWriter.close();//close the indexwriter
        }
    }
	
	
	public static void main(String[] args) throws Exception { 
		long start= System.currentTimeMillis();
		PropertyConfigurator.configure("config/log4j.properties");
		Configuration conf= new Configuration(); 
		Job job= Job.getInstance(conf, "word count"); 
		
		job.setJarByClass(lucene.class);
		job.setMapperClass(indexmap.class); 
	
		FileInputFormat.addInputPath(job, new Path("hdfs://young0:9000/data/material4")); 
		FileOutputFormat.setOutputPath(job, new Path("hdfs://young0:9000/data/test3")); 
		if(job.waitForCompletion(true)) {
			long end = System.currentTimeMillis();
			System.out.println("the total time is: "+(end-start)+"ms");
			System.exit(1);				
		}
		System.exit(job.waitForCompletion(true) ? 0 : 1); 
		
		
		}

}
