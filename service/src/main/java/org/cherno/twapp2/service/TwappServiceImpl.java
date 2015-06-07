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

    public String getSuggestedLocation(String name) {
        return null;
    }

    public List<String> getLocations(String name) {
        TwappDAO twappDAO = new TwappDAOImpl();
        List<String> result = new ArrayList<>();

        TwappData twappData;
        try {
            twappData = twappDAO.getTwitterData(name);
            for(String location : twappData.getFollowersLocations()) result.add(location);
            for(String location : twappData.getFriendsLocations()) result.add(location);
        } catch (TwitterDAOExeption twitterDAOExeption) {
            twitterDAOExeption.printStackTrace();
        }
        return result;
    }
}
