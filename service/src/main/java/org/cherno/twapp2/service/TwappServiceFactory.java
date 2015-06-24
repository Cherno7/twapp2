package org.cherno.twapp2.service;

import org.apache.commons.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 24.06.2015.
 */
public class TwappServiceFactory {
    private static Map<Configuration, TwappService> serviceMap = new HashMap<>();

    public static TwappService getService(Configuration externalConfiguration) {
        if (serviceMap.containsKey(externalConfiguration)) {
            return serviceMap.get(externalConfiguration);
        }
        else {
            TwappService twappService = new TwappServiceImpl(externalConfiguration);
            serviceMap.put(externalConfiguration, twappService);
            return twappService;
        }
    }
}
