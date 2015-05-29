package org.cherno.twapp2.twitterDAO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created on 29.05.2015.
 */
public class resultJson {
        List<User> users;
        private int next_cursor;
        private int next_cursor_str;
        private int previous_cursor;
        private int previous_cursor_str;

    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getNext_cursor_str() {
        return next_cursor_str;
    }

    public void setNext_cursor_str(int next_cursor_str) {
        this.next_cursor_str = next_cursor_str;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public int getPrevious_cursor_str() {
        return previous_cursor_str;
    }

    public void setPrevious_cursor_str(int previous_cursor_str) {
        this.previous_cursor_str = previous_cursor_str;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

