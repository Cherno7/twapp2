package org.cherno.twapp2.twitterDAO;

import java.util.Collections;
import java.util.List;

/**
 * Created on 24.06.2015.
 */
public class LocationsRequestResult {
    private List<String> results;
    private int status;
    private int limit;

    public LocationsRequestResult() {
        this.results = Collections.emptyList();
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
