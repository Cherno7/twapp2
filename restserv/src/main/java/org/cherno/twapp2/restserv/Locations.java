package org.cherno.twapp2.restserv;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created on 08.06.2015.
 */
@XmlRootElement
public class Locations {
    private List<String> locations;

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}