package com.itplh.absengine.util;

import com.itplh.absengine.script.DelayVariable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayUtils {

    public static void delay(long timeout, TimeUnit timeUnit) {
        AssertUtils.assertNotNull(timeUnit, "time unit is required.");
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Interrupted while sleeping.");
        }
    }

    public static void delay(DelayVariable variable) {
        if (variable == null
                || variable.getDelay() < 0
                || variable.getDelayTimeUnit() == null) {
            variable = DelayVariable.defaultDelay();
        }
        long delay = variable.getDelay();
        TimeUnit delayTimeUnit = variable.getDelayTimeUnit();
        delay(delay, delayTimeUnit);
    }

}
