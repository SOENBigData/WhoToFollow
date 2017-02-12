package WhoToFollow.pkg;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WhotoFollow {
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		try {
			executeMapReduce(args, InvertMapper.class, InvertReducer.class);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean executeMapReduce(String[] args, Class mapperClass, Class reducerClass)
			throws IOException, InterruptedException, ClassNotFoundException {
		Configuration conf1 = new Configuration();
		Job job1 = new Job(conf1, "Who To Follow");
		job1.setJarByClass(WhotoFollow.class);
		job1.setMapperClass(mapperClass);
		job1.setReducerClass(reducerClass);
		job1.setOutputKeyClass(IntWritable.class);
		job1.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job1, new Path(args[0]));
		FileSystem fs = FileSystem.get(conf1);
		/*Check if output path (args[1])exist or not*/
		if(fs.exists(new Path(args[1]))){
			/*If exist delete the output path*/
			fs.delete(new Path(args[1]),true);
		}
		FileOutputFormat.setOutputPath(job1, new Path(args[1]));
		if(job1.waitForCompletion(true)){
			Job job2 = Job.getInstance(conf1, "JOB_2");
			job2.setMapperClass(FilterMapper.class);
			job2.setReducerClass(FilterReducer.class);
			//job2.setReducerClass(reducerClass);
		//	job2.setInputFormatClass(KeyValueTextInputFormat.class);
			job2.setOutputKeyClass(IntWritable.class);
			job2.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job2, new Path(args[1]));
			if(fs.exists(new Path(args[2]))){
				/*If exist delete the output path*/
				fs.delete(new Path(args[2]),true);
			}
			FileOutputFormat.setOutputPath(job2, new Path(args[2]));
			job2.waitForCompletion(true);			
		}
		return true;

	}


}