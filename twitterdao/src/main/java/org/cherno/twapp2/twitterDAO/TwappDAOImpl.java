package org.cherno.twapp2.twitterDAO;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.cherno.twapp2.twitterDAO.http.CachingTwitterHttpClient;
import org.cherno.twapp2.twitterDAO.http.TwitterHttpClient;
import org.cherno.twapp2.twitterDAO.http.TwitterResponse;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28.05.2015.
 */
public class TwappDAOImpl implements TwappDAO{

    private CompositeConfiguration configuration = new CompositeConfiguration();
    private static final Logger logger = LoggerFactory.getLogger(TwappDAOImpl.class);
    private TwitterHttpClient client;

    public TwappDAOImpl() {
        try {
            this.configuration.addConfiguration(new PropertiesConfiguration("twitterdao.properties"));
        } catch (ConfigurationException e) {
            logger.error("{}", e.getMessage());
        }
        this.client  = new CachingTwitterHttpClient(configuration);
    }

    public TwappDAOImpl(Configuration externalConfiguration) {
        this.configuration.addConfiguration(externalConfiguration);
        try {
            this.configuration.addConfiguration(new PropertiesConfiguration("twitterdao.properties"));
        } catch (ConfigurationException e) {
            logger.error("{}", e.getMessage());
        }
        this.client  = new CachingTwitterHttpClient(configuration);
    }

    public TwappData getTwitterData(String userName) throws TwitterDAOExeption {
        String friendsURL = this.configuration.getString("twitterdao.friendsListURL");
        String followersURL = this.configuration.getString("twitterdao.followerListURL");
        int limit = this.configuration.getInt("twitterdao.querylimit");
        TwappData twappData = new TwappData();

        LocationsRequestResult result = this.getLocations(followersURL, userName, limit);
        twappData.setFollowersLocations(result.getResults());
        twappData.setFollowersResponseStatus(result.getStatus());
        twappData.setFollowersRemainingLimit(result.getLimit());

        result = this.getLocations(friendsURL, userName, limit);
        twappData.setFriendsLocations(result.getResults());
        twappData.setFriendsResponseStatus(result.getStatus());
        twappData.setFriendsRemainingLimit(result.getLimit());

        return twappData;
    }

    private LocationsRequestResult getLocations(String url, String userName, int limit) throws TwitterDAOExeption {
        LocationsRequestResult results = new LocationsRequestResult();
        TwitterResponse twitterResponse;

        long cursor = -1;
        int counter = 0;
        List<String> locations = new ArrayList<>();
        String finalURL = url + userName + "&cursor=%d";
        try {
            do {
                twitterResponse = client.getTwitterResponse(String.format(finalURL, cursor));
                results.setLimit(twitterResponse.getLimit());
                results.setStatus(twitterResponse.getStatus());

                if (twitterResponse.getStatus() == 401) {
                    logger.error("Bad authentication data");
                    return results;
                }
                if (twitterResponse.getStatus() == 404) {
                    logger.error("User not found");
                    return results;
                }
                if (twitterResponse.getStatus() == 429){
                    logger.warn("Hit the limit while obtaining the followers list");
                    break;
                }

                String json = twitterResponse.getBody();
                ResultJson rj = this.unmarshallResponse(new StreamSource(new StringReader(json)));
                for (User user : rj.getUsers())
                    if (user.getLocation() != null)
                        locations.add(user.getLocation());

                cursor = rj.getNextCursor();
                if (twitterResponse.getLimit() != -1)
                    counter += rj.getUsers().size();
                logger.info("Processing URL = {}, count = {}", String.format(finalURL, cursor), counter);
            } while (cursor != 0 && counter < limit);

            if (cursor != 0) results.setStatus(429);
            results.setResults(locations);
        } catch (JAXBException e) {
            throw new TwitterDAOExeption(e.getMessage(), e.getCause());
        }
        return results;
    }

    private ResultJson unmarshallResponse(StreamSource streamSource) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(ResultJson.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
        return unmarshaller.unmarshal(streamSource, ResultJson.class).getValue();
    }
}