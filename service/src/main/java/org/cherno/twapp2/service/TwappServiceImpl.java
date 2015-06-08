package org.cherno.twapp2.service;

import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappDAOImpl;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 31.05.2015.
 */
public class TwappServiceImpl implements TwappService{
    TwappDAO twappDAO;

    public TwappServiceImpl() {
        this.twappDAO = new TwappDAOImpl();
    }

    private List<String> getFullListOfLocations(String name, int limit, boolean skipEmpty) {
        List<String> result = new ArrayList<>();
        TwappData twappData;
        try {
            twappData = twappDAO.getTwitterData(name, limit);
            for (String location : twappData.getFollowersLocations())
                if (!(location.isEmpty() && skipEmpty))
                    result.add(location);
            for(String location : twappData.getFriendsLocations())
                if (!(location.isEmpty() && skipEmpty))
                    result.add(location);
        } catch (TwitterDAOExeption twitterDAOExeption) {
            twitterDAOExeption.printStackTrace();
        }

        return  result;
    }

    public String getSuggestedLocation(String name, int limit, boolean skipEmpty) {
        List<String> fullList = getFullListOfLocations(name, limit, skipEmpty);
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();

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
        String result = Util.getMostCommon(list1) + ", " + Util.getMostCommon(list2) + " : " + Util.getMostCommon(list3);

        return result;
    }

    public List<String> getLocations(String name, int limit, boolean skipEmpty) {
        return getFullListOfLocations(name, limit, skipEmpty);
    }
}
