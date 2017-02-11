package pymk.pkg1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

//PairsReducer is used for creating the key values pair so that the negative values are carried forward to the next map
//and hence to remove the already following accounts from being suggested again.
public class PairsReducer extends Reducer<IntWritable,IntWritable,IntWritable,Text>{
	public void reduce(IntWritable key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException{
		IntWritable user=key;
		StringBuffer sb = new StringBuffer("");
		while (values.iterator().hasNext()) {
			Integer value = values.iterator().next().get();
			sb.append(value.toString() + " ");
		}
		Text result = new Text(sb.toString());
		context.write(user, result);   
	} 

}
