package com.itplh.absengine.util;

import com.itplh.absengine.script.DelayVariable;

import java.util.concurrent.TimeUnit;

public class DelayUtils {

    public static final long DEFAULT_DELAY = 1500L;
    public static final TimeUnit DEFAULT_DELAY_TIME_UNIT = TimeUnit.MILLISECONDS;

    public static void delay(long timeout, TimeUnit timeUnit) {
        AssertUtils.assertNotNull(timeUnit, "time unit is required.");
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public static void defaultDelay() {
        delay(DEFAULT_DELAY, DEFAULT_DELAY_TIME_UNIT);
    }

    public static void delay(DelayVariable variable) {
        if (variable == null
                || variable.getDelay() < 0
                || variable.getDelayTimeUnit() == null) {
            defaultDelay();
            return;
        }
        long delay = variable.getDelay();
        TimeUnit delayTimeUnit = variable.getDelayTimeUnit();
        delay(delay, delayTimeUnit);
    }

}
