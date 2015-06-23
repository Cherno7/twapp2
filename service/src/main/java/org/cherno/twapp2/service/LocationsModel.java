package org.cherno.twapp2.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created on 11.06.2015.
 */
public class LocationsModel {
    private List<String> locations;
    private Map<String, Integer> headers;

    public LocationsModel() {
        this.locations = Collections.emptyList();
        this.headers = Collections.emptyMap();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationsModel)) return false;

        LocationsModel that = (LocationsModel) o;

        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (locations != null ? !locations.equals(that.locations) : that.locations != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locations != null ? locations.hashCode() : 0;
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }
}
