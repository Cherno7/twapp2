package org.cherno.twapp2.restserv;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created on 08.06.2015.
 */
@XmlRootElement
public class SuggestedLocation {
    @XmlElement(name="suggested_location")
    private String suggestedLocation;
    @XmlElement(name="optional_location")
    private String optionalLocation;
    private String status;
    private String limits;

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
}
