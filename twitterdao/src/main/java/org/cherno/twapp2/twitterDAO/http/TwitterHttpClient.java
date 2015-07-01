package org.cherno.twapp2.twitterDAO.http;

import org.cherno.twapp2.twitterDAO.TwitterDAOExeption;

/**
 * Created on 30.06.2015.
 */
public interface TwitterHttpClient {
    public TwitterResponse getTwitterResponse(String url) throws TwitterDAOExeption;
}
