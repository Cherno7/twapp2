package org.cherno.twapp2.service;

import java.util.List;
import java.util.Map;

/**
 * Created on 11.06.2015.
 */
public class LocationsModel {
    private List<String> locations;
    private Map<String, Integer> headers;

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Integer> headers) {
        this.headers = headers;
    }
}
