package com.itplh.absengine.util;

import com.itplh.absengine.script.DelayVariable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RetryUtilTest {

    @Test
    void testRetry1() {
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(this::exampleMethod1, 3, DelayVariable.buildSeconds(1L)));
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(() -> exampleMethod2(36D), 3, DelayVariable.buildSeconds(1L)));
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(() -> {
            exampleMethod3();
            return "";
        }, 3, DelayVariable.buildSeconds(1L)));
    }

    @Test
    void testRetry2() {
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(this::exampleMethod1, 3));
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(() -> exampleMethod2(36D), 3));
        TryCatchUtil.ignoreException(() -> RetryUtil.retry(() -> {
            exampleMethod3();
            return "";
        }, 3));
    }

    public double exampleMethod1() {
        // Some example code that may fail
        double random = Math.random();
        log.info("random: {}", random);
        if (random < 0.7) {
            throw new RuntimeException("An error occurred.");
        }
        return random;
    }

    public double exampleMethod2(double num) {
        // Some example code that may fail
        double random = Math.random();
        log.info("random: {}", random);
        if (random < 0.7) {
            throw new RuntimeException("An error occurred.");
        }
        return num + random;
    }

    public void exampleMethod3() {
        // Some example code that may fail
        double random = Math.random();
        log.info("random: {}", random);
        if (random < 0.7) {
            throw new RuntimeException("An error occurred.");
        }
    }

}