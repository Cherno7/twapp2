package org.cherno.twapp2.twitterDAO;

/**
 * Created on 31.05.2015.
 */
public class TwappData {
    private Iterable <String> followersLocations;
    private Iterable <String> friendsLocations;
    private int followersResponseStatus;
    private int friendsResponseStatus;

    private int followersRemainingLimit;
    private int friendsRemainingLimit;


    public Iterable<String> getFollowersLocations() {
        return followersLocations;
    }

    public void setFollowersLocations(Iterable<String> followersLocations) {
        this.followersLocations = followersLocations;
    }

    public Iterable<String> getFriendsLocations() {
        return friendsLocations;
    }

    public void setFriendsLocations(Iterable<String> friendsLocations) {
        this.friendsLocations = friendsLocations;
    }

    public int getFollowersResponseStatus() {
        return followersResponseStatus;
    }

    public void setFollowersResponseStatus(int followersResponseStatus) {
        this.followersResponseStatus = followersResponseStatus;
    }

    public int getFollowersRemainingLimit() {
        return followersRemainingLimit;
    }

    public void setFollowersRemainingLimit(int followersRemainingLimit) {
        this.followersRemainingLimit = followersRemainingLimit;
    }

    public int getFriendsRemainingLimit() {
        return friendsRemainingLimit;
    }

    public void setFriendsRemainingLimit(int friendsRemainingLimit) {
        this.friendsRemainingLimit = friendsRemainingLimit;
    }

    public int getFriendsResponseStatus() {
        return friendsResponseStatus;
    }

    public void setFriendsResponseStatus(int friendsResponseStatus) {
        this.friendsResponseStatus = friendsResponseStatus;
    }
}
