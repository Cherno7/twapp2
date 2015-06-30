package org.cherno.twapp2.twitterDAO.http;

/**
 * Created on 30.06.2015.
 */
public class TwitterResponse {
    private String body;
    private int status;
    private int limit;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
