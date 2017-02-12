package pymk.pkg1;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<Object,Text,IntWritable,IntWritable>{
	public void map(Object key,Text values,Context context) throws IOException,InterruptedException{
		//Convert the string to tokens
		System.out.println(values);
		StringTokenizer st1 = new StringTokenizer(values.toString().substring(2));
		while(st1.hasMoreTokens()){
			Integer usr1=Integer.parseInt(st1.nextToken().toString().trim());
			StringTokenizer st2 = new StringTokenizer(values.toString().substring(2));
			while(st2.hasMoreTokens()){
				Integer usr2=Integer.parseInt(st2.nextToken().toString().trim());
				context.write(new IntWritable(usr1),new IntWritable(usr2));
			}
		}
	}
}
