package org.cherno.twapp2.twitterDAO;

/**
 * Created on 31.05.2015.
 */
public interface TwappDAO {
    public TwappData getTwitterData (String userName, int limit) throws TwitterDAOExeption;
}
