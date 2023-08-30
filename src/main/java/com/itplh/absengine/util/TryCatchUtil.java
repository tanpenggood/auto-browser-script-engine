package com.itplh.absengine.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class TryCatchUtil {

    public static <R> R ignoreException(Supplier<R> method) {
        R result = null;
        try {
            result = method.get();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            // ignore
        }
        return result;
    }

}
