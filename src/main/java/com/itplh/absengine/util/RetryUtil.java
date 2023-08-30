package com.itplh.absengine.util;

import com.itplh.absengine.script.DelayVariable;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtil {

    /***
     * 重试方法
     * 对目标方法进行包装，当执行目标方法抛出异常时，将会进行重试
     * @param method 执行的目标方法
     * @param maxRetries 最大重试次数
     * @param firstRetryDelayVariable 执行第一次重试的延迟
     * @return
     * @param <R>
     * @throws RuntimeException retry fail
     */
    public static <R> R retry(Supplier<R> method, int maxRetries, DelayVariable firstRetryDelayVariable) {
        R result = null;
        int retryCount = 0;
        while (retryCount <= maxRetries) {
            try {
                result = method.get();
                log.debug("Success after {} retries.", retryCount);
                break;
            } catch (Throwable e) {
                if (retryCount >= maxRetries) {
                    log.warn("Failed to execute task after {} retries.", maxRetries);
                    throw new RuntimeException(e);
                }
                retryCount++;
                DelayVariable delayVariable = decayDelay(retryCount, firstRetryDelayVariable);
                log.info("Retry the {}th time, after waiting for {} {}.", retryCount, delayVariable.getDelay(), delayVariable.getDelayTimeUnit());
                DelayUtils.delay(delayVariable);
            }
        }
        return result;
    }

    public static <R> R retry(Supplier<R> method, int maxRetries) {
        return retry(method, maxRetries, DelayVariable.defaultDelay());
    }

    /**
     * 随着重试次数的增加，逐渐衰减
     *
     * @param retryCount
     * @param firstRetryDelayVariable
     * @return
     */
    private static DelayVariable decayDelay(int retryCount, DelayVariable firstRetryDelayVariable) {
        if (retryCount <= 1) {
            return firstRetryDelayVariable == null ? DelayVariable.defaultDelay() : firstRetryDelayVariable;
        }
        if (retryCount < 16) {
            if (firstRetryDelayVariable == null || firstRetryDelayVariable.getDelay() == 0) {
                long delay = retryCount < 16 ? (4L * retryCount - 3) : 100L;
                return DelayVariable.buildSeconds(delay);
            }
            return new DelayVariable(3L * (retryCount - 1) * firstRetryDelayVariable.getDelay(), firstRetryDelayVariable.getDelayTimeUnit());
        }
        return DelayVariable.buildSeconds(100L);
    }

}
