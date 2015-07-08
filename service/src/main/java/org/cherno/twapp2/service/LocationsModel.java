package org.cherno.twapp2.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created on 11.06.2015.
 */
public class LocationsModel {
    private List<String> locations;
    private String status;

    public LocationsModel() {
        this.locations = Collections.emptyList();
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
