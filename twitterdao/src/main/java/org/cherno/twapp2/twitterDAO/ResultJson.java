package org.cherno.twapp2.twitterDAO;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created on 29.05.2015.
 */
public class ResultJson {
        List<User> users;
        private long nextCursor;

    @XmlElement(name="next_cursor")
    public long getNextCursor() {
        return nextCursor;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setNextCursor(long nextCursor) {
        this.nextCursor = nextCursor;
    }
}

