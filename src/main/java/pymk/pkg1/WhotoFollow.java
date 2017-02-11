package pymk.pkg1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.logging.Logger;

public class WhotoFollow {

    /**
     * *****************
     */
    /**
     * Mapper      *
     */
    /**
     * *****************
     */
   public static class FollowerpairMapper extends Mapper<Object, Text, IntWritable, IntWritable> {

        public void map(Object key, Text values, Context context) throws IOException, InterruptedException {
        	Logger log = Logger.getLogger(FollowerpairMapper.class.getName());
            StringTokenizer st = new StringTokenizer(values.toString());
            log.info("the values in string are " + st.toString());
            IntWritable user = new IntWritable(Integer.parseInt(st.nextToken()));
            
            log.info("the values in user are " + user);
            // 'friends' will store the list of friends of user 'user'
            ArrayList<Integer> friends = new ArrayList<>();
            // First, go through the list of all friends of user 'user' and emit 
            // (user,-friend)
            // 'friend1' will be used in the emitted pair
            IntWritable follower1 = new IntWritable();
            while (st.hasMoreTokens()) {
               
                IntWritable followee = new IntWritable(Integer.parseInt(st.nextToken()));
                context.write(followee,user); 
                log.info("the values in postive frinds new are " + followee +","+ user);
                
                log.info("the values in friend new are " + follower1.toString());
               context.write(follower1,user); ////replaced user to friend2
               //log.info("the value of friend is" + friend
                
               log.info("the values in context friend1 " + follower1 +","+ user);
               
               
               friends.add(followee.get());
                // save the friends of user 'user' for later
            }
            
            log.info("the values in followees are " + friends.toString());
            // Now we can emit all (a,b) and (b,a) pairs
            // where a!=b and a & b are friends of user 'user'.
            // We use the same algorithm as before.
           ArrayList<Integer> followedBy = new ArrayList<>();
            //log.info("the values in seen friends are " + seenFriends.toString());
            // The element in the pairs that will be emitted.
            IntWritable follower2 = new IntWritable();
            for (Integer followee : friends) {
            	follower1.set(followee);
                log.info("the values in seen friend1 now are " + follower1.toString());
               
                for (Integer seenFollower : followedBy) {
                	follower2.set(seenFollower);                
                    context.write(follower1, follower2);
                    log.info("the values in context seenfriend1 " + follower1 +","+ follower2);
                    
                    context.write(follower2, follower1);
                    log.info("the values in context seenfriend2 " + follower2 +","+ follower1);
                }
                followedBy.add(follower1.get());
                log.info("the values in seen seenFriends now are " + followedBy.toString());
            }
        }
    }

	
	

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "people you may know");
        job.setJarByClass(Pymk1.class);
        job.setMapperClass(FollowerpairMapper.class);
       // job.setMapperClass(InverseMapper.class);
      //  job.setReducerClass(CountReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
