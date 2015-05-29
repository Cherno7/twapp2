package org.cherno.twapp2.twitterDAO;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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
            HttpGet request = new HttpGet("https://api.twitter.com/1.1/followers/ids.json?screen_name=BlizzardCSEU_EN");
            consumer.sign(request);

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.length() > 0) {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
