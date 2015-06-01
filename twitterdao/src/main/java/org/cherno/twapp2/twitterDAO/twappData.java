package org.cherno.twapp2.twitterDAO;

/**
 * Created on 31.05.2015.
 */
public class TwappData {
    private Iterable <String> followersLocations;
    private Iterable <String> friendsLocations;
    private int responseStatus;
    private int remainingLimit;

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

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getRemainingLimit() {
        return remainingLimit;
    }

    public void setRemainingLimit(int remainingLimit) {
        this.remainingLimit = remainingLimit;
    }
}
