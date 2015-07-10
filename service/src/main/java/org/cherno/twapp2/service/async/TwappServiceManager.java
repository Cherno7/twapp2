package org.cherno.twapp2.service.async;

/**
 * Created on 10.07.2015.
 */
public interface TwappServiceManager extends Runnable{
    public void addTask(Task task);
    public boolean isTaskDone(Task task);
    public Object getTaskResult(Task task);
}
