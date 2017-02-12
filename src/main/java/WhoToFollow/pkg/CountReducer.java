package pymk.pkg1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CountReducer extends Reducer<IntWritable, IntWritable, IntWritable, Text> {



    // The reduce method       
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        IntWritable user = key;
        // 'existingFriends' will store the friends of user 'user'
        // (the negative values in 'values').
        ArrayList<Integer> existingFriends = new ArrayList();
        // 'recommendedUsers' will store the list of user ids recommended
        // to user 'user'
        ArrayList<Integer> recommendedUsers = new ArrayList<>();
        while (values.iterator().hasNext()) {
            int value = values.iterator().next().get();
            if (value > 0) {
                recommendedUsers.add(value);
            } else {
                existingFriends.add(value);
            }
        }
        // 'recommendedUsers' now contains all the positive values in 'values'.
        // We need to remove from it every value -x where x is in existingFriends.
        // See javadoc on Predicate: https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html
        for (Integer friend : existingFriends) {
            recommendedUsers.removeIf(new Predicate<Integer>() {
                @Override
                public boolean test(Integer t) {
                    return t.intValue() == -friend.intValue();
                }
            });
        }
        ArrayList<Recommendation> recommendations = new ArrayList<>();
        // Builds the recommendation array
        for (Integer userId : recommendedUsers) {
            Recommendation p = Recommendation.find(userId, recommendations);
            if (p == null) {
                recommendations.add(new Recommendation(userId));
            } else {
                p.addCommonFriend();
            }
        }
        // Sorts the recommendation array
        // See javadoc on Comparator at https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
        recommendations.sort(new Comparator<Recommendation>() {
            @Override
            public int compare(Recommendation t, Recommendation t1) {
                return -Integer.compare(t.getNCommonFriends(), t1.getNCommonFriends());
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

