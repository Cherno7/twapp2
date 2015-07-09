package org.cherno.twapp2.restserv.rm;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created on 09.07.2015.
 */
@XmlRootElement
@XmlType(propOrder={"resource", "counter", "oneMinuteRate", "fiveMinuteRate", "fifteenMinuteRate"})
public class Status {
    private String resource;
    private long counter;
    private double oneMinuteRate;
    private double fiveMinuteRate;
    private double fifteenMinuteRate;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public double getOneMinuteRate() {
        return oneMinuteRate;
    }

    public void setOneMinuteRate(double oneMinuteRate) {
        this.oneMinuteRate = oneMinuteRate;
    }

    public double getFiveMinuteRate() {
        return fiveMinuteRate;
    }

    public void setFiveMinuteRate(double fiveMinuteRate) {
        this.fiveMinuteRate = fiveMinuteRate;
    }

    public double getFifteenMinuteRate() {
        return fifteenMinuteRate;
    }

    public void setFifteenMinuteRate(double fifteenMinuteRate) {
        this.fifteenMinuteRate = fifteenMinuteRate;
    }
}
