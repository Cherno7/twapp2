package org.cherno.twapp2.twitterDAO;

/**
 * Created on 31.05.2015.
 */
public class TwappData {
    private Iterable <String> followersLocations;
    private Iterable <String> friendsLocations;
    private String userLocation;
    private int responseStatus;
    private int currentLimit;

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

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }
}
