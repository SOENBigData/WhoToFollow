package WhoToFollow.pkg;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * InvertReducer creates an inverted list of followers and followees
 **/

public class InvertReducer extends Reducer<IntWritable,IntWritable,IntWritable,Text>{
	public void reduce(IntWritable key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
		IntWritable user=key; // user identified by the key
		StringBuffer sb = new StringBuffer("");
		while (values.iterator().hasNext()) {
			Integer value = values.iterator().next().get();
			sb.append(value.toString() + " ");
		}
		Text result = new Text(sb.toString());
		context.write(user, result); // emits key value pair  
	} 

}
