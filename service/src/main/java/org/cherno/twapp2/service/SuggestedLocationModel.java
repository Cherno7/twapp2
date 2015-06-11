package org.cherno.twapp2.service;

import java.util.Map;

/**
 * Created on 11.06.2015.
 */
public class SuggestedLocationModel {
    private String suggestedLocation;
    private String optionalLocation;
    private Map<String, Integer> headers;

    public String getSuggestedLocation() {
        return suggestedLocation;
    }

    public void setSuggestedLocation(String suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }

    public String getOptionalLocation() {
        return optionalLocation;
    }

    public void setOptionalLocation(String optionalLocation) {
        this.optionalLocation = optionalLocation;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Integer> headers) {
        this.headers = headers;
    }
}
