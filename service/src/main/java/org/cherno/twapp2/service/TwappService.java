package org.cherno.twapp2.service;

/**
 * Created on 07.06.2015.
 */
public interface TwappService {
    SuggestedLocationModel getSuggestedLocation(String name, boolean skipEmpty);
    LocationsModel getLocations(String name, boolean skipEmpty);
    String getCurrentTwitterLimits();
}
