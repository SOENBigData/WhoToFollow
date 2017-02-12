package WhoToFollow.pkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class FilterMapper extends Mapper<Object,Text,IntWritable,IntWritable>{
	
	public void map(Object key, Text values, Context context) throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(values.toString());
        IntWritable user = new IntWritable(Integer.parseInt(st.nextToken()));
        // 'followersID' will store the list of followers of user 'user'
        ArrayList<Integer> followersID = new ArrayList<>();
        // First, go through the list of all followers of user 'user' and emit 
        // (user,followee)
        // 'followee' will be used in the emitted pair
        IntWritable follower1 = new IntWritable();
        while (st.hasMoreTokens()) {
            Integer nextVal = Integer.parseInt(st.nextToken());
           
            follower1.set(nextVal);
            if(nextVal<  0 )
            {
            context.write(user, follower1);
            }
            followersID.add(nextVal);
        }
        ArrayList<Integer> prevFollowers = new ArrayList<>();
        // The element in the pairs that will be emitted.
       //  emit all (a,b) and (b,a) pairs
        // where a!=b and a & b are followers of user 'user'.
        IntWritable follower2 = new IntWritable();
        for (Integer friend : followersID) {
        	follower1.set(friend);
            for (Integer seenFriend : prevFollowers) {
                follower2.set(seenFriend);
                if(!(follower1.get()<  0 || follower2.get() <0))
                {
                	
                context.write(follower1, follower2); 
                context.write(follower2, follower1);
                
                }
            }
            prevFollowers.add(follower1.get());
        }
    }
}
