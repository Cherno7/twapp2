package org.cherno.twapp2.twitterDAO;

/**
 * Created on 01.06.2015.
 */
public class TwitterDAOExeption extends Exception {
    public TwitterDAOExeption(String message) {
        super(message);
    }

    public TwitterDAOExeption() {
    }

    public TwitterDAOExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitterDAOExeption(Throwable cause) {
        super(cause);
    }

    public TwitterDAOExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
