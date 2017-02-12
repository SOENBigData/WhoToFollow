package WhoToFollow.pkg;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * *****************
 */
/**
 * InvertMapper to do indexing of the userlist(input) to get followers and followees
 *
/*This class creates key values pair for the reducer
 **/

public class InvertMapper extends Mapper<Object,Text,IntWritable,IntWritable>{
	public void map(Object key,Text values,Context context) throws IOException,InterruptedException{		
		StringTokenizer st = new StringTokenizer(values.toString());//splitting  into token 		
		IntWritable user=new IntWritable(Integer.parseInt(st.nextToken()));  // storing one token of value  as a key 		
		IntWritable userFollowerIDs = new IntWritable();		
		while(st.hasMoreTokens()){			
			/*storing and overriding token of value in the integer value
			 * emit key-value pairs to get followers and followees* */		
			Integer followerId=Integer.parseInt(st.nextToken());			
			userFollowerIDs.set(followerId);
			context.write(userFollowerIDs,user);
			userFollowerIDs.set(-followerId);	
			context.write(user,userFollowerIDs);    
		}
	}
}
