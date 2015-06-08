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

    public String getSuggestedLocation() {
        return suggestedLocation;
    }

    public void setSuggestedLocation(String suggestedLocation) {
        this.suggestedLocation = suggestedLocation;
    }
}
