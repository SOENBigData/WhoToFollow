package WhoToFollow.pkg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FilterReducer extends Reducer<IntWritable,IntWritable,IntWritable,Text>{
	private static class Recommendation {

        // Attributes
        private int followerId;
        private int nCommonFollowers;

        // Constructor
        public Recommendation(int friendId) {
            this.followerId = friendId;
            // A recommendation must have at least 1 common friend
            this.nCommonFollowers = 1;
        }

        // Getters
        public int getFollowerId() {
            return followerId;
        }

        public int getNCommonFollowers() {
            return nCommonFollowers;
        }

        // Other methods
        // Increments the number of common friends
        public void addCommonFollower() {
            nCommonFollowers++;
        }

        // String representation used in the reduce output            
        public String toString() {
            return followerId + "(" + nCommonFollowers + ")";
        }

        // Finds a representation in an array
        public static Recommendation find(int followerId, ArrayList<Recommendation> recommendations) {
            for (Recommendation p : recommendations) {
                if (p.getFollowerId() == followerId) {
                    return p;
                }
            }
            // Recommendation was not found!
            return null;
        }
    }

    // The reduce method       
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        IntWritable user = key;
        // 'existingFriends' will store the friends of user 'user'
        // (the negative values in 'values').
        ArrayList<Integer> existingFollowers = new ArrayList();
        // 'recommendedUsers' will store the list of user ids recommended
        // to user 'user'
        ArrayList<Integer> recommendedFollowers = new ArrayList<>();
        while (values.iterator().hasNext()) {
            int value = values.iterator().next().get();
            if (value > 0) {
                recommendedFollowers.add(value);
            } else {
                existingFollowers.add(value);
            }
        }
        // 'recommendedUsers' now contains all the positive values in 'values'.
        // We need to remove from it every value -x where x is in existingFriends.
        // See javadoc on Predicate: https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html
        for (Integer follower : existingFollowers) {
            recommendedFollowers.removeIf(new Predicate<Integer>() {
                @Override
                public boolean test(Integer t) {
                    return t.intValue() == -follower.intValue();
                }
            });
        }
        ArrayList<Recommendation> recommendations = new ArrayList<>();
        // Builds the recommendation array
        for (Integer userId : recommendedFollowers) {
            Recommendation p = Recommendation.find(userId, recommendations);
            if (p == null) {
                recommendations.add(new Recommendation(userId));
            } else {
                p.addCommonFollower();
            }
        }
        // Sorts the recommendation array
        // See javadoc on Comparator at https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
        recommendations.sort(new Comparator<Recommendation>() {
            @Override
            public int compare(Recommendation t, Recommendation t1) {
                return -Integer.compare(t.getNCommonFollowers(), t1.getNCommonFollowers());
            }
        });
        // Builds the output string that will be emitted
        StringBuffer sb = new StringBuffer(""); // Using a StringBuffer is more efficient than concatenating strings
        for (int i = 0; i < recommendations.size() && i < 10; i++) {
            Recommendation p = recommendations.get(i);
            sb.append(p.toString() + " ");
        }
        Text result = new Text(sb.toString());
        context.write(user, result);
    }
}
