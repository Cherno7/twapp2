package org.cherno.twapp2.restserv;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created on 08.06.2015.
 */
@XmlRootElement
public class Locations {
    private String status;
    private String limits;
    private List<String> locations;

    public String getLimits() {
        return limits;
    }

    public void setLimits(String limits) {
        this.limits = limits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}