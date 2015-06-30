package org.cherno.twapp2.twitterDAO.http;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.configuration.Configuration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created on 30.06.2015.
 */
public class CachingTwitterHttpClient implements TwitterHttpClient {
    private Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(CachingTwitterHttpClient.class);
    private OAuthConsumer consumer;

    public CachingTwitterHttpClient(Configuration configuration) {
        this.configuration = configuration;
        this.consumer = initOAuth(configuration);
    }

    public TwitterResponse getTwitterResponse(String url) throws TwitterDAOExeption {
        return this.getTwitterResponse(url, false);
    }

    public TwitterResponse getTwitterResponse(String url, boolean caching) throws TwitterDAOExeption {

        TwitterResponse twitterResponse = new TwitterResponse();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request;
        HttpResponse response;
        request = new HttpGet(url);

        try {
            this.consumer.sign(request);
            response = client.execute(request);
            int responseStatus = response.getStatusLine().getStatusCode();
            twitterResponse.setStatus(responseStatus);
            twitterResponse.setLimit(Integer.parseInt(response.getFirstHeader("x-rate-limit-remaining").getValue()));
            twitterResponse.setBody(EntityUtils.toString(response.getEntity()));
        } catch (IOException | OAuthException e) {
            throw new TwitterDAOExeption(e.getMessage(), e.getCause());
        }

        return twitterResponse;
    }

    private OAuthConsumer initOAuth(Configuration configuration){
        String consumerKey = configuration.getString("twitterdao.consumerKey");
        String consumerSecret = configuration.getString("twitterdao.consumerSecret");
        String accessToken = configuration.getString("twitterdao.accessToken");
        String accessTokenSecret = configuration.getString("twitterdao.accessTokenSecret");

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, accessTokenSecret);
        return consumer;
    }
}
