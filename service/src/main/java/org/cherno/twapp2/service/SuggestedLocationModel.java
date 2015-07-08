package org.cherno.twapp2.service;


/**
 * Created on 11.06.2015.
 */
public class SuggestedLocationModel {
    private String suggestedLocation;
    private String status;

    public SuggestedLocationModel() {
        this.suggestedLocation = "";
    }

    public String getSuggestedLocation() {
        return suggestedLocation;
    }

    public void setSuggestedLocation(String suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
