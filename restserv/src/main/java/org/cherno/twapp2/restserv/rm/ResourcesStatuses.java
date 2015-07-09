package org.cherno.twapp2.restserv.rm;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created on 09.07.2015.
 */
@XmlRootElement
public class ResourcesStatuses {
    List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
