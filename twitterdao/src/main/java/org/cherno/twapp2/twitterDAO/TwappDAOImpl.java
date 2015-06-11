package org.cherno.twapp2.twitterDAO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
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
import java.util.Properties;

/**
 * Created on 28.05.2015.
 */
public class TwappDAOImpl implements TwappDAO{

    private static final String followerListURL = "https://api.twitter.com/1.1/followers/list.json?count=100&skip_status=true&include_user_entities=false&screen_name=";
    private static final String friendsListURL = "https://api.twitter.com/1.1/friends/list.json?count=100&skip_status=true&include_user_entities=false&screen_name=";

    private static final Logger logger = LoggerFactory.getLogger(TwappDAOImpl.class);

    public TwappData getTwitterData(String userName, int limit) throws TwitterDAOExeption {
        Properties oAuthProperties = new Properties();
        TwappData twappData = new TwappData();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request;
        HttpResponse response;
        long cursor = -1;

        int counter = 0;

        try {
            oAuthProperties.load(new FileReader(new File("twapi.properties")));
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oAuthProperties.getProperty("consumerKey"), oAuthProperties.getProperty("consumerSecret"));
            consumer.setTokenWithSecret(oAuthProperties.getProperty("accessToken"), oAuthProperties.getProperty("accessTokenSecret"));

            List<String> followerList= new ArrayList<>();
            do {
                request = new HttpGet(followerListURL + userName + "&cursor=" + cursor);
                consumer.sign(request);
                response = client.execute(request);

                int responseStatus = response.getStatusLine().getStatusCode();
                twappData.setFollowersResponseStatus(responseStatus);
                //not safe
                twappData.setFollowersRemainingLimit(Integer.parseInt(response.getFirstHeader("x-rate-limit-remaining").getValue()));

                if (responseStatus == 401) {
                    logger.error("Bad authentication data");
                    return twappData;
                }
                if (responseStatus == 404) {
                    logger.error("Not found");
                    return twappData;
                }
                if (responseStatus == 429){
                    logger.warn("Hit the limit while obtaining the followers list");
                    break;
                }

                JAXBContext jc = JAXBContext.newInstance(ResultJson.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
                unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

                StreamSource json = new StreamSource(response.getEntity().getContent());
                ResultJson rj = unmarshaller.unmarshal(json, ResultJson.class).getValue();

                for (User user : rj.getUsers())
                    if (user.getLocation() != null)
                        followerList.add(user.getLocation());

                cursor = rj.getNextCursor();
                counter += rj.getUsers().size();
                logger.info("Processing list of the followers. Cursor = {}, count = {}", cursor, counter);
            } while (cursor != 0 && counter < limit);
            twappData.setFollowersLocations(followerList);

            cursor = -1;
            counter = 0;
            List<String> friendsList= new ArrayList<>();
            do {
                request = new HttpGet(friendsListURL + userName + "&cursor=" + cursor);
                consumer.sign(request);
                response = client.execute(request);

                int responseStatus = response.getStatusLine().getStatusCode();
                twappData.setFriendsResponseStatus(responseStatus);
                //not safe
                twappData.setFriendsRemainingLimit(Integer.parseInt(response.getFirstHeader("x-rate-limit-remaining").getValue()));

                if (responseStatus == 404) {
                    logger.error("Not found");
                    return twappData;
                }
                if (responseStatus == 401) {
                    logger.error("Bad authentication data");
                    return twappData;
                }
                if (responseStatus == 429){
                    logger.warn("Hit the limit while obtaining the friends list");
                    break;
                }

                JAXBContext jc = JAXBContext.newInstance(ResultJson.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
                unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

                StreamSource json = new StreamSource(response.getEntity().getContent());
                ResultJson rj = unmarshaller.unmarshal(json, ResultJson.class).getValue();

                for (User user : rj.getUsers())
                    if (user.getLocation() != null)
                        friendsList.add(user.getLocation());

                cursor = rj.getNextCursor();
                counter += rj.getUsers().size();
                logger.info("Processing list of the followers. Cursor = {}, count = {}", cursor, counter);
            } while (cursor != 0 && counter < limit);
            twappData.setFriendsLocations(friendsList);

        } catch (IOException|JAXBException|OAuthException e) {
            throw new TwitterDAOExeption(e.getMessage(), e.getCause());
        }

        return twappData;
    }
}
