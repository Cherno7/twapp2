package org.cherno.twapp2.service.async;

import org.cherno.twapp2.service.LocationsModel;
import org.cherno.twapp2.service.SuggestedLocationModel;
import org.cherno.twapp2.service.TwappService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created on 10.07.2015.
 */
public class TwappServiceManagerSingleThread implements TwappServiceManager {
    private static final Logger logger = LoggerFactory.getLogger(TwappServiceManagerSingleThread.class);
    private final TwappService twappService;
    private List<Task> finishedTasks;
    private Queue<Task> queue;
    private boolean isWorking = false;

    public TwappServiceManagerSingleThread(TwappService twappService) {
        this.twappService = twappService;
        this.finishedTasks = new ArrayList<>();
        this.queue = new LinkedList<>();
    }

    public void addTask(Task task) {
        if (!this.queue.contains(task)) this.queue.offer(task);
        logger.debug("Task added");
    }

    public boolean isTaskDone(Task task) {
        logger.debug("Task checked. finished size = {}, queue length = {}", finishedTasks.size(), queue.size());
        return finishedTasks.contains(task);
    }

    public Object getTaskResult(Task task) {
        Object obj = finishedTasks.get(finishedTasks.indexOf(task)).getResult();
        finishedTasks.remove(task);
        return obj;
    }

    @Override
    public void run() {
        this.isWorking = true;
        do {
            if(!queue.isEmpty()){
                Task task = queue.poll();
                if (task.gettClass() == LocationsModel.class){
                    task.setResult(twappService.getLocations(task.getUserName(), task.isSkipEmpty()));
                    finishedTasks.add(task);
                } else if(task.gettClass() == SuggestedLocationModel.class){
                    logger.info("Got some task for {}::{}", task.getUserName(),  task.isSkipEmpty());
                    SuggestedLocationModel sl = twappService.getSuggestedLocation(task.getUserName(), task.isSkipEmpty());
                    logger.info("sl::{}", sl.getSuggestedLocation());
                    task.setResult(sl);
                    finishedTasks.add(task);
                }
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){
                logger.info("'Async' thread has been interrapted");
            }
        } while (isWorking);
    }

    public void shutdown() {
        this.isWorking = false;
    }
}
