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
import org.junit.Test;



public class Project {
	public static class IndexMapper extends Mapper<Object, Text, Text, Text>{ 
	    private FileSplit fs;
	    private String s;
	    private int n=0;
	    private Text word = new Text(); 
	    private Text word2 = new Text();
	   
	    public void map(Object key, Text value, Mapper<Object,Text,Text,Text>.Context context) throws IOException, InterruptedException{ 
	    	
	       fs=(FileSplit) context.getInputSplit();//to split the filepath 
	       
		   String str = value.toString().replaceAll("[\\pP\\pS\\pN]",""); //convert character other than letters to spaces
		   
		   StringTokenizer itr= new StringTokenizer(str); 
		   n++;
		   while (itr.hasMoreTokens()) { 
			   word.set(itr.nextToken() + "-" + fs.getPath().getName().toString()+":"); 
			   //make the file name and the key word conbine
			   s=Integer.toString(n);
			   word2.set(s);// make word2 get the s (line number)
			   context.write(word,word2); //out put the key word + file name +line number
			   }
		   }
	}
	
	public static class IndexCom extends Reducer<Text,Text,Text,Text> { 
    	private Text text = new Text();  
	    private Text line = new Text();
	    public void reduce(Text key, Iterable<Text> values, Context context ) throws IOException, InterruptedException{ 
	    	    String[] s = key.toString().split("-");//split the key word and file name
	    	    String s1 =s[0];
	    	    String s2 = s[1];//to store these s1 and s2
	    	    
	    	    for (Text val: values) { 
	    	    	s2 +=""+val.toString()+" "; 
	    	    }
	    	    // combine the file name and the line number
	    	    line.set(s2); //make the line string  get the value of value
	    	    text.set(s1.toString());
	    	    context.write(text,line); //out put the file name + line number
	    	    } 
	    }

	public static class Indexre extends Reducer<Text,Text,Text,Text>{
		Text text = new Text();
		public void reduce(Text key,Iterable<Text> values, Context 
	    		context ) throws IOException, InterruptedException{
			String s = new String();
			//combine the word + file name
			for(Text value :values) {
				s = s + "["+ value.toString()+"]";
			}//can be get the form of ["file name" ]
			text.set(s);//text can get the value of s
			context.write(key, text);//out the word : file names
			
		}
	}
	

	public static void main(String[] args) throws Exception { 
		long start= System.currentTimeMillis();
		
		PropertyConfigurator.configure("config/log4j.properties");
		Configuration conf= new Configuration(); 
		Job job= Job.getInstance(conf, "word count"); 
		job.setJarByClass(Project.class); 
		
		job.setMapperClass(IndexMapper.class); 
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setCombinerClass(IndexCom.class); 
		job.setReducerClass(Indexre.class); 
		job.setOutputKeyClass(Text.class); 
		job.setOutputValueClass(Text.class); 
		FileInputFormat.addInputPath(job, new Path("hdfs://young0:9000/data/material4")); 
		FileOutputFormat.setOutputPath(job, new Path("hdfs://young0:9000/data/projectaus")); 
		//FileInputFormat.addInputPath(job, new Path(args[0])); 
		//FileOutputFormat.setOutputPath(job, new Path(args[1])); 
		if(job.waitForCompletion(true)) {
			long end = System.currentTimeMillis();
			System.out.println("the total time is: "+(end-start)+"ms");
			System.exit(1);				
		}
		System.exit(job.waitForCompletion(true) ? 0 : 1); 
		
		}
	


}
