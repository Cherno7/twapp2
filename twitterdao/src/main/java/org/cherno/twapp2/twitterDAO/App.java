package org.cherno.twapp2.twitterDAO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Properties;

/**
 * Created on 28.05.2015.
 */
public class App {
    public static void main(String[] args) {
        Properties oauthProperties = new Properties();
        try {
            oauthProperties.load(new FileReader(new File("twapi.properties")));

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(oauthProperties.getProperty("consumerKey"), oauthProperties.getProperty("consumerSecret"));
            consumer.setTokenWithSecret(oauthProperties.getProperty("accessToken"), oauthProperties.getProperty("accessTokenSecret"));
            HttpGet request = new HttpGet("https://api.twitter.com/1.1/followers/list.json?screen_name=gasenvagen&count=200&skip_status=true&include_user_entities=false");
            consumer.sign(request);

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                    sb.append(line);
            }

            String resultJSON = sb.toString();

            JAXBContext jc = JAXBContext.newInstance(resultJson.class);

            Unmarshaller unmarshaller = jc.createUnmarshaller();

            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");

            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

            StreamSource json = new StreamSource(new StringReader(resultJSON));

            resultJson rj = unmarshaller.unmarshal(json, resultJson.class).getValue();

            System.out.println(rj.getNext_cursor());
            for (User user : rj.getUsers())
                System.out.println(user.getLocation());






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
