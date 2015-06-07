package org.cherno.twapp2.service;

import java.util.List;

/**
 * Created on 07.06.2015.
 */
public interface TwappService {
    String getSuggestedLocation(String name);
    List<String> getLocations(String name, int limit);
}
