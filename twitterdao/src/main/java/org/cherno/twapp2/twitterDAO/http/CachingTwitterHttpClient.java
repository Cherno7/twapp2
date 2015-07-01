package org.cherno.twapp2.twitterDAO.http;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created on 30.06.2015.
 */
public class CachingTwitterHttpClient implements TwitterHttpClient {
    private Configuration configuration;
    private static final Logger logger = LoggerFactory.getLogger(CachingTwitterHttpClient.class);
    private OAuthConsumer consumer;
    private CacheAccess<String, String> cache;
    private HttpClient client;

    public CachingTwitterHttpClient(Configuration configuration) {
        this.configuration = configuration;
        this.consumer = initOAuth(configuration);
        this.cache = initCache(configuration);
        this.client = HttpClientBuilder.create().build();
    }

    private CacheAccess<String, String> initCache(Configuration configuration) {
        CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream("/cache.ccf"));
            prop.setProperty("jcs.auxiliary.DC.attributes.DiskPath", configuration.getString("twitterdao.cachedir"));
            ccm.configure(prop);
        } catch (IOException e) {
            logger.error("cache.ccf not found in classpath");
        }
        return JCS.getInstance("twappCache");
    }

    public TwitterResponse getTwitterResponse(String url) throws TwitterDAOExeption {
        TwitterResponse twitterResponse = new TwitterResponse();

        if (configuration.getBoolean("twitterdao.caching") && cache.get(url) != null){
            twitterResponse.setStatus(200);
            twitterResponse.setLimit(-1);
            twitterResponse.setBody(cache.get(url));
            return twitterResponse;
        }
        else {
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

            if (configuration.getBoolean("twitterdao.caching"))
                cache.put(url, twitterResponse.getBody());
            return twitterResponse;
        }
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
