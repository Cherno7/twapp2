package org.cherno.twapp2.service;

import java.util.Collections;
import java.util.Map;

/**
 * Created on 11.06.2015.
 */
public class SuggestedLocationModel {
    private String suggestedLocation;
    private String optionalLocation;
    private Map<String, Integer> headers;

    public SuggestedLocationModel() {
        this.suggestedLocation = "";
        this.optionalLocation = "";
        this.headers = Collections.emptyMap();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuggestedLocationModel)) return false;

        SuggestedLocationModel that = (SuggestedLocationModel) o;

        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (optionalLocation != null ? !optionalLocation.equals(that.optionalLocation) : that.optionalLocation != null)
            return false;
        if (suggestedLocation != null ? !suggestedLocation.equals(that.suggestedLocation) : that.suggestedLocation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = suggestedLocation != null ? suggestedLocation.hashCode() : 0;
        result = 31 * result + (optionalLocation != null ? optionalLocation.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }
}
