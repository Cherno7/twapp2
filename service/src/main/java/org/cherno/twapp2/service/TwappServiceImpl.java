package org.cherno.twapp2.service;

import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappDAOImpl;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 31.05.2015.
 */
public class TwappServiceImpl implements TwappService{
    TwappDAO twappDAO;

    public TwappServiceImpl() {
        this.twappDAO = new TwappDAOImpl();
    }

    public Map<String, Object> getSuggestedLocation(String name, int limit, boolean skipEmpty) {
        List<String> fullList = new ArrayList<>();
        TwappData twappData;
        Map<String, Object> result = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();

        try {
            twappData = twappDAO.getTwitterData(name, limit);
            for (String location : twappData.getFollowersLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);
            for(String location : twappData.getFriendsLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);


            //first iteration, country-city pairs
            for (String str : fullList) {
                str = str.trim().toLowerCase();
                if (str.contains(",")) {
                    String[] splitStr = str.split(",", 2);
                    splitStr[0] = splitStr[0].trim();
                    splitStr[1] = splitStr[1].trim();
                    if (list1.contains(splitStr[0])) {
                        list1.add(splitStr[0]);
                        list2.add(splitStr[1]);
                    }
                    else if (list2.contains(splitStr[0])){
                        list2.add(splitStr[0]);
                        list1.add(splitStr[1]);
                    }
                    else {
                        list1.add(splitStr[0]);
                        list2.add(splitStr[1]);
                    }
                }
            }
            //second iteration

            for (String str : fullList) {
                str = str.trim().toLowerCase();
                if (!str.contains(",")) {
                    if (list1.contains(str)) {
                        list1.add(str);
                    }
                    else if (list2.contains(str)){
                        list2.add(str);
                    }
                    else {
                        list3.add(str);
                    }
                }
            }

            result.put("Location", Util.getMostCommon(list1) + ", " + Util.getMostCommon(list2));
            result.put("Optional", Util.getMostCommon(list3));
            result.put("FriendsLimit", twappData.getFriendsRemainingLimit());
            result.put("FollowersLimit", twappData.getFollowersRemainingLimit());
            result.put("FriendsStatus", twappData.getFriendsResponseStatus());
            result.put("FollowersStatus", twappData.getFollowersResponseStatus());

        } catch (TwitterDAOExeption twitterDAOExeption) {
            twitterDAOExeption.printStackTrace();
        }
        return result;
    }

    public Map<String, Object> getLocations(String name, int limit, boolean skipEmpty) {
        List<String> fullList = new ArrayList<>();
        TwappData twappData;
        Map<String, Object> result = new HashMap<>();

        try {
            twappData = twappDAO.getTwitterData(name, limit);
            for (String location : twappData.getFollowersLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);
            for(String location : twappData.getFriendsLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);

            result.put("Locations", fullList);
            result.put("FriendsLimit", twappData.getFriendsRemainingLimit());
            result.put("FollowersLimit", twappData.getFollowersRemainingLimit());
            result.put("FriendsStatus", twappData.getFriendsResponseStatus());
            result.put("FollowersStatus", twappData.getFollowersResponseStatus());


        } catch (TwitterDAOExeption twitterDAOExeption) {
            twitterDAOExeption.printStackTrace();
        }
        return result;
    }
}
