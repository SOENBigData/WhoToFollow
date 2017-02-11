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

public class Pymk1 {

    /**
     * *****************
     */
    /**
     * Mapper      *
     */
    /**
     * *****************
     */
    public static class AllPairsMapper extends Mapper<Object, Text, IntWritable, IntWritable> {

        public void map(Object key, Text values, Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(values.toString());
            IntWritable user = new IntWritable(Integer.parseInt(st.nextToken()));
            // 'friends' will store the list of friends of user 'user'
            ArrayList<Integer> friends = new ArrayList<>();
            // First, go through the list of all friends of user 'user' and emit 
            // (user,-friend)
            // 'friend1' will be used in the emitted pair
            IntWritable friend1 = new IntWritable();
            while (st.hasMoreTokens()) {
                Integer friend = Integer.parseInt(st.nextToken());
                friend1.set(-friend);
                context.write(user, friend1);
                friends.add(friend); // save the friends of user 'user' for later
            }
            // Now we can emit all (a,b) and (b,a) pairs
            // where a!=b and a & b are friends of user 'user'.
            // We use the same algorithm as before.
            ArrayList<Integer> seenFriends = new ArrayList<>();
            // The element in the pairs that will be emitted.
            IntWritable friend2 = new IntWritable();
            for (Integer friend : friends) {
                friend1.set(friend);
                for (Integer seenFriend : seenFriends) {
                    friend2.set(seenFriend);
                    context.write(friend1, friend2);
                    context.write(friend2, friend1);
                }
                seenFriends.add(friend1.get());
            }
        }
    }
    
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "people you may know");
        job.setJarByClass(Pymk1.class);
        job.setMapperClass(AllPairsMapper.class);
        //job.setReducerClass(CountReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}