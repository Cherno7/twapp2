package org.cherno.twapp2.service;

import org.apache.commons.configuration.Configuration;
import org.cherno.twapp2.service.city.CityChecker;
import org.cherno.twapp2.service.city.CityCheckerGeoNames;
import org.cherno.twapp2.service.country.CountryChecker;
import org.cherno.twapp2.service.country.CountryCheckerISO3166;
import org.cherno.twapp2.service.storage.MemoryStorage;
import org.cherno.twapp2.service.storage.Storage;
import org.cherno.twapp2.twitterDAO.TwappDAO;
import org.cherno.twapp2.twitterDAO.TwappDAOImpl;
import org.cherno.twapp2.twitterDAO.TwappData;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 31.05.2015.
 */
public class TwappServiceImpl implements TwappService{
    private static final Logger logger = LoggerFactory.getLogger(TwappServiceImpl.class);

    private TwappDAO twappDAO;
    private Storage storage;
    private CountryChecker countryChecker;
    private CityChecker cityChecker;

    public TwappServiceImpl(Configuration externalConfiguration) {
        this.twappDAO = new TwappDAOImpl(externalConfiguration);
        this.storage = MemoryStorage.getInstance("twapp");
        this.countryChecker = new CountryCheckerISO3166();
        this.cityChecker = new CityCheckerGeoNames(externalConfiguration);
    }

    public TwappServiceImpl(TwappDAO twappDAO) {
        this.twappDAO = twappDAO;
        this.storage = MemoryStorage.getInstance("twapp");
        this.countryChecker = new CountryCheckerISO3166();
        this.cityChecker = new CityCheckerGeoNames();
    }

    public SuggestedLocationModel getSuggestedLocation(String name, boolean skipEmpty) {
        List<String> fullList = new ArrayList<>();
        TwappData twappData;
        SuggestedLocationModel suggestedLocationModel = new SuggestedLocationModel();

        List<String> countries = new ArrayList<>();
        List<String> cities = new ArrayList<>();

        try {
            twappData = twappDAO.getTwitterData(name);
            for (String location : twappData.getFollowersLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);
            for (String location : twappData.getFriendsLocations())
                if (!(location.isEmpty() && skipEmpty))
                    fullList.add(location);

            if (twappData.getFriendsRemainingLimit() >= 0)
                storage.update("friendsLimit", twappData.getFriendsRemainingLimit());
            if (twappData.getFollowersRemainingLimit() >= 0)
                storage.update("followersLimit", twappData.getFollowersRemainingLimit());

            logger.info("Number of locations received = {}", fullList.size());

            splitLocations(fullList, countries, cities);

            logger.info("Sizes: Countries = {}, Cities = {}", countries.size(), cities.size());
            suggestedLocationModel.setSuggestedLocation(Util.getMostCommon(countries) + ", " + Util.getMostCommon(cities));
            suggestedLocationModel.setStatus(getStatus(twappData));

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

            if (twappData.getFriendsRemainingLimit() >= 0)
                storage.update("friendsLimit", twappData.getFriendsRemainingLimit());
            if (twappData.getFollowersRemainingLimit() >= 0)
                storage.update("followersLimit", twappData.getFollowersRemainingLimit());

            logger.info("Number of locations received = {}", fullList.size());

            locationsModel.setLocations(fullList);
            locationsModel.setStatus(getStatus(twappData));

        } catch (TwitterDAOExeption twitterDAOExeption) {
            logger.error("{}", twitterDAOExeption.getMessage());
        }
        return locationsModel;
    }

    public String getCurrentTwitterLimits() {
        String friendsLimit = (storage.contain("friendsLimit")) ? "" + storage.get("friendsLimit") : "from cache";
        String followersLimit = (storage.contain("followersLimit")) ? "" + storage.get("followersLimit") : "from cache";

        return "friends=" + friendsLimit + ", followers=" + followersLimit;
    }

    private String getStatus(TwappData twappData) {
        int followersStatus = twappData.getFollowersResponseStatus();
        int friendsStatus = twappData.getFriendsResponseStatus();
        String status = "Oops, how did you get this? (" + followersStatus + ", " + friendsStatus + ")";

        if (followersStatus == 200 && friendsStatus == 200)
            status = "Complete set of data";
        if (followersStatus == 401 || friendsStatus == 401)
            status = "Not authorized";
        if (followersStatus == 404 || friendsStatus == 404)
            status = "User not found on Twitter";
        if (followersStatus == 429 || friendsStatus == 429)
            status = "Incomplete set of data";
        return status;
    }

    private void splitLocations(List<String> fullList, List<String> countries, List<String> cities) {
        for (String str : fullList) {
            str = str.trim().toLowerCase();
            String[] splitStr = str.split(",");
            for (String s: splitStr){
                s = s.trim();
                if (countryChecker.isCountry(s)) {
                    countries.add(countryChecker.getCountryCode(s));
                } else if(countryChecker.isCountryCode(s)) {
                    countries.add(s);
                } else if(cityChecker.isCity(s)){
                    cities.add(cityChecker.getCityName(s));
                }
            }
        }
    }
}
