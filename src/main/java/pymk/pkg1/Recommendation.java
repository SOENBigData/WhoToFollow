package pymk.pkg1;

import java.util.ArrayList;


// A private class to describe a recommendation.
// A recommendation has a friend id and a number of friends in common.
public  class Recommendation {

    // Attributes
    private int friendId;
    private int nCommonFriends;

    // Constructor
    public Recommendation(int friendId) {
        this.friendId = friendId;
        // A recommendation must have at least 1 common friend
        this.nCommonFriends = 1;
    }

    // Getters
    public int getFriendId() {
        return friendId;
    }

    public int getNCommonFriends() {
        return nCommonFriends;
    }

    // Other methods
    // Increments the number of common friends
    public void addCommonFriend() {
        nCommonFriends++;
    }

    // String representation used in the reduce output            
    public String toString() {
        return friendId + "(" + nCommonFriends + ")";
    }

    // Finds a representation in an array
    public static Recommendation find(int friendId, ArrayList<Recommendation> recommendations) {
        for (Recommendation p : recommendations) {
            if (p.getFriendId() == friendId) {
                return p;
            }
        }
        // Recommendation was not found!
        return null;
    }
}
