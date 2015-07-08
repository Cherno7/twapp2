package org.cherno.twapp2.service.storage;

/**
 * Created on 08.07.2015.
 */
public interface Storage {
    public Object get(String key);
    public void put(String key, Object value);
    public void update(String key, Object value);
    public void remove(String key);
    public boolean contain(String key);
}
