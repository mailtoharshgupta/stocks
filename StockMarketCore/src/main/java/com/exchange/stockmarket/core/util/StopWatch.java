package com.exchange.stockmarket.core.util;


import java.util.concurrent.TimeUnit;

/**
 * @author Harsh Gupta on {2/10/18}
 */
public class StopWatch {
    private long startTime;

    public StopWatch() {
        startTime = System.nanoTime();
    }

    public long elapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
}
