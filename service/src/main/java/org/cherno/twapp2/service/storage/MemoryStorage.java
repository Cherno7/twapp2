package org.cherno.twapp2.service.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 08.07.2015.
 */
public class MemoryStorage implements Storage {
    private static Map<String, Storage> instances = new ConcurrentHashMap<>();

    private Map<String, Object> storage;

    public static Storage getInstance(String instanceName) {
        if (instances.containsKey(instanceName)) {
            return instances.get(instanceName);
        } else {
            Storage storage = new MemoryStorage();
            instances.put(instanceName, storage);
            return storage;
        }
    }

    private MemoryStorage() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Object get(String key) {
        return this.storage.get(key);
    }

    public void put(String key, Object value) {
        this.storage.put(key, value);
    }

    public void update(String key, Object value) {
        if (this.storage.containsKey(key)){
            this.storage.replace(key, value);
        } else {
            this.storage.put(key, value);
        }
    }

    public void remove(String key) {
        this.storage.remove(key);
    }

    public boolean contain(String key) {
        return this.storage.containsKey(key);
    }
}
