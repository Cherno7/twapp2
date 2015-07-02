package org.cherno.twapp2.twitterDAO.cache;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created on 02.07.2015.
 */
public class TwappCacheJCSImpl implements TwappCache {
    private CacheAccess<String, String> cache;
    private static final Logger logger = LoggerFactory.getLogger(TwappCacheJCSImpl.class);

    public TwappCacheJCSImpl(Configuration configuration) {
        CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream("/cache.ccf"));
            prop.setProperty("jcs.auxiliary.DC.attributes.DiskPath", configuration.getString("twitterdao.cachedir"));
            ccm.configure(prop);
        } catch (IOException e) {
            logger.error("cache.ccf not found in classpath");
        }
        this.cache = JCS.getInstance("twappCache");
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }
}
