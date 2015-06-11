package org.cherno.twapp2.service;

import java.util.Map;

/**
 * Created on 07.06.2015.
 */
public interface TwappService {
    Map<String, Object> getSuggestedLocation(String name, int limit, boolean skipEmpty);
    Map<String, Object> getLocations(String name, int limit, boolean skipEmpty);
}
