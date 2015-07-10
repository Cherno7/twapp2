package org.cherno.twapp2.service.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 10.07.2015.
 */
public class Task<T> {
    private static Map<String, Task> tasks = new HashMap<>();

    private final String userName;
    private final boolean skipEmpty;
    private Class<T> tClass;
    private Object result;

    public static Task getTask(Class tClass, String userName, boolean skipEmpty) {
        String id = userName + (skipEmpty?"1":"0");
        if (tasks.containsKey(id)) return tasks.get(id);
        return new Task<>(tClass, userName, skipEmpty);
    }

    private Task(Class<T> tClass, String userName, boolean skipEmpty) {
        this.userName = userName;
        this.skipEmpty = skipEmpty;
        this.tClass = tClass;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isSkipEmpty() {
        return skipEmpty;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Class<T> gettClass() {
        return tClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (skipEmpty != task.skipEmpty) return false;
        if (!tClass.equals(task.tClass)) return false;
        if (!userName.equals(task.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + (skipEmpty ? 1 : 0);
        result = 31 * result + tClass.hashCode();
        return result;
    }
}
