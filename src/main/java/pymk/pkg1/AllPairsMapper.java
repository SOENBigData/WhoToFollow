package pymk.pkg1;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * *****************
 */
/**
 * Mapper      *
 */
/**
 * *****************
 */
public class AllPairsMapper extends Mapper<Object,Text,IntWritable,IntWritable>{
	public void map(Object key,Text values,Context context) throws IOException,InterruptedException{
		//Convert the string to tokens
		StringTokenizer st = new StringTokenizer(values.toString());
		//Parse the key
		IntWritable user=new IntWritable(Integer.parseInt(st.nextToken()));
		//IntWritable context for the account which the user follows
		IntWritable userFollowsAccount = new IntWritable();
		// First, go through the list of all the users the 'user' is following and emit
		// (user,-userFollowsAccount) to keep track of the accounts which the user already follows.
		// 'userFollowsAccount' will be used in the emitted pair
		while(st.hasMoreTokens()){
			Integer account=Integer.parseInt(st.nextToken());
			userFollowsAccount.set(account);
			context.write(userFollowsAccount,user);
			userFollowsAccount.set(-account);
			context.write(user,userFollowsAccount);
		}
	}
}
