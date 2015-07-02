package org.cherno.twapp2.twitterDAO.cache;

/**
 * Created on 02.07.2015.
 */
public interface TwappCache {
    public String get(String key);
    public void put(String key, String value);
    public void remove(String key);
}
