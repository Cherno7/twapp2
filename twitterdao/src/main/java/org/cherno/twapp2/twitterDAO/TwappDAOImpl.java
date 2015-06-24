package org.cherno.twapp2.twitterDAO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
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
    private OAuthConsumer consumer;

    public TwappDAOImpl() {
        try {
            this.configuration.addConfiguration(new PropertiesConfiguration("twitterdao.properties"));
        } catch (ConfigurationException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public TwappDAOImpl(Configuration externalConfiguration) {
        this.configuration.addConfiguration(externalConfiguration);
        try {
            this.configuration.addConfiguration(new PropertiesConfiguration("twitterdao.properties"));
        } catch (ConfigurationException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public TwappData getTwitterData(String userName, int limit) throws TwitterDAOExeption {
        String friendsURL = this.configuration.getString("twitterdao.friendsListURL");
        String followersURL = this.configuration.getString("twitterdao.followerListURL");

        this.initOAuth();
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

    private void initOAuth(){
        String consumerKey = this.configuration.getString("twitterdao.consumerKey");
        String consumerSecret = this.configuration.getString("twitterdao.consumerSecret");
        String accessToken = this.configuration.getString("twitterdao.accessToken");
        String accessTokenSecret = this.configuration.getString("twitterdao.accessTokenSecret");

        this.consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        this.consumer.setTokenWithSecret(accessToken, accessTokenSecret);
    }

    private LocationsRequestResult getLocations(String url, String userName, int limit) throws TwitterDAOExeption {
        LocationsRequestResult results = new LocationsRequestResult();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request;
        HttpResponse response;
        long cursor = -1;
        int counter = 0;
        List<String> locations = new ArrayList<>();
        try {
            do {
                request = new HttpGet(url + userName + "&cursor=" + cursor);
                this.consumer.sign(request);
                response = client.execute(request);

                int responseStatus = response.getStatusLine().getStatusCode();
                results.setStatus(responseStatus);
                //not safe
                results.setLimit(Integer.parseInt(response.getFirstHeader("x-rate-limit-remaining").getValue()));

                if (responseStatus == 401) {
                    logger.error("Bad authentication data");
                    return results;
                }
                if (responseStatus == 404) {
                    logger.error("User not found");
                    return results;
                }
                if (responseStatus == 429){
                    logger.warn("Hit the limit while obtaining the followers list");
                    break;
                }

                ResultJson rj = this.unmarshallResponse(new StreamSource(response.getEntity().getContent()));
                for (User user : rj.getUsers())
                    if (user.getLocation() != null)
                        locations.add(user.getLocation());

                cursor = rj.getNextCursor();
                counter += rj.getUsers().size();
                logger.info("Processing URL = {}. Cursor = {}, count = {}", url + userName, cursor, counter);
            } while (cursor != 0 && counter < limit);
            results.setResults(locations);
        } catch (IOException | JAXBException | OAuthException e) {
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