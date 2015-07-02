package org.cherno.twapp2.service;

import org.apache.commons.configuration.Configuration;
import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappDAOImpl;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 31.05.2015.
 */
public class TwappServiceImpl implements TwappService{
    private static final Logger logger = LoggerFactory.getLogger(TwappServiceImpl.class);

    private TwappDAO twappDAO;

    public TwappServiceImpl(Configuration externalConfiguration) {
        this.twappDAO = new TwappDAOImpl(externalConfiguration);
    }

    public TwappServiceImpl(TwappDAO twappDAO) {
        this.twappDAO = twappDAO;
    }

    public SuggestedLocationModel getSuggestedLocation(String name, boolean skipEmpty) {
        List<String> fullList = new ArrayList<>();
        TwappData twappData;
        SuggestedLocationModel suggestedLocationModel = new SuggestedLocationModel();

        List<String> leftSubstrings = new ArrayList<>();
        List<String> rightSubstrings = new ArrayList<>();
        List<String> atomicStrings = new ArrayList<>();

        try {
            twappData = twappDAO.getTwitterData(name);
            for (String location : twappData.getFollowersLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);
            for (String location : twappData.getFriendsLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);

            logger.info("Number of locations received = {}", fullList.size());

            //first iteration, country-city pairs
            for (String str : fullList) {
                str = str.trim().toLowerCase();
                if (str.contains(",")) {
                    String[] splitStr = str.split(",", 2);
                    splitStr[0] = splitStr[0].trim();
                    splitStr[1] = splitStr[1].trim();
                    if (leftSubstrings.contains(splitStr[0])) {
                        leftSubstrings.add(splitStr[0]);
                        rightSubstrings.add(splitStr[1]);
                    }
                    else if (rightSubstrings.contains(splitStr[0])){
                        rightSubstrings.add(splitStr[0]);
                        leftSubstrings.add(splitStr[1]);
                    }
                    else {
                        leftSubstrings.add(splitStr[0]);
                        rightSubstrings.add(splitStr[1]);
                    }
                }
            }
            //second iteration

            for (String str : fullList) {
                str = str.trim().toLowerCase();
                if (!str.contains(",")) {
                    if (leftSubstrings.contains(str)) {
                        leftSubstrings.add(str);
                    }
                    else if (rightSubstrings.contains(str)){
                        rightSubstrings.add(str);
                    }
                    else {
                        atomicStrings.add(str);
                    }
                }
            }

            logger.info("Sizes: List1 = {}, List2 = {}, List3 = {}", leftSubstrings.size(), rightSubstrings.size(), atomicStrings.size());
            suggestedLocationModel.setSuggestedLocation(Util.getMostCommon(leftSubstrings) + ", " + Util.getMostCommon(rightSubstrings));
            suggestedLocationModel.setOptionalLocation(Util.getMostCommon(atomicStrings));

            Map<String, Integer> headers = new HashMap<>();
            headers.put("twitter-followers-status", twappData.getFollowersResponseStatus());
            headers.put("twitter-friends-status", twappData.getFriendsResponseStatus());
            headers.put("twitter-followers-limit", twappData.getFollowersRemainingLimit());
            headers.put("twitter-friends-limit", twappData.getFriendsRemainingLimit());


            suggestedLocationModel.setHeaders(headers);
        } catch (TwitterDAOExeption twitterDAOExeption) {
            logger.error("{}", twitterDAOExeption.getMessage());
        }
        return suggestedLocationModel;
    }

    public LocationsModel getLocations(String name, boolean skipEmpty) {
        List<String> fullList = new ArrayList<>();
        TwappData twappData;
        LocationsModel locationsModel = new LocationsModel();

        try {
            twappData = twappDAO.getTwitterData(name);
            if (twappData.getFollowersLocations() != null)
                for (String location : twappData.getFollowersLocations())
                    if (!(location.isEmpty() && skipEmpty))
                        fullList.add(location);
            if (twappData.getFriendsLocations() != null)
                for(String location : twappData.getFriendsLocations())
                    if (!(location.isEmpty() && skipEmpty))
                        fullList.add(location);

            logger.info("Number of locations received = {}", fullList.size());

            locationsModel.setLocations(fullList);

            Map<String, Integer> headers = new HashMap<>();

            headers.put("twitter-followers-status", twappData.getFollowersResponseStatus());
            headers.put("twitter-friends-status", twappData.getFriendsResponseStatus());
            headers.put("twitter-followers-limit", twappData.getFollowersRemainingLimit());
            headers.put("twitter-friends-limit", twappData.getFriendsRemainingLimit());

            locationsModel.setHeaders(headers);

        } catch (TwitterDAOExeption twitterDAOExeption) {
            logger.error("{}", twitterDAOExeption.getMessage());
        }
        return locationsModel;
    }
}
