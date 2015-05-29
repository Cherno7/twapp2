package org.cherno.twapp2.twitterDAO;


/**
 * Created on 29.05.2015.
 */
public class User {
            private int id;
            private String name;
            private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
