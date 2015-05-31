package org.cherno.twapp2.twitterDAO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Properties;

/**
 * Created on 28.05.2015.
 */
public class TwappDAOImpl implements TwappDAO{

    private static final String followerListURL = "https://api.twitter.com/1.1/followers/list.json?count=200&skip_status=true&include_user_entities=false&screen_name=";
    private static final String friendsListURL = "https://api.twitter.com/1.1/friends/list.json?count=200&skip_status=true&include_user_entities=false&screen_name=";


    public TwappData getTwitterData(String userName) {
        Properties oAuthProperties = new Properties();
        TwappData twappData = new TwappData();
        try {
            oAuthProperties.load(new FileReader(new File("twapi.properties")));
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oAuthProperties.getProperty("consumerKey"), oAuthProperties.getProperty("consumerSecret"));
            consumer.setTokenWithSecret(oAuthProperties.getProperty("accessToken"), oAuthProperties.getProperty("accessTokenSecret"));

            HttpClient client = HttpClientBuilder.create().build();



            HttpGet request = new HttpGet(followerListURL + userName);
            consumer.sign(request);
            HttpResponse response = client.execute(request);

            twappData.setResponseStatus(response.getStatusLine().getStatusCode());

            JAXBContext jc = JAXBContext.newInstance(ResultJson.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

            StreamSource json = new StreamSource(response.getEntity().getContent());
            ResultJson rj = unmarshaller.unmarshal(json, ResultJson.class).getValue();

            for(User user : rj.getUsers()) System.out.println(user.getLocation());

        } catch (IOException|JAXBException|OAuthException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static void main(String[] args) {
        TwappDAO twappDAO = new TwappDAOImpl();
        TwappData twappData = twappDAO.getTwitterData("gasenvagen");
    }
}
