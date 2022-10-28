package com.itplh.absengine.constant;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public enum TimeUnitEnum {

    MILLISECONDS("毫秒", TimeUnit.MILLISECONDS),
    SECONDS("秒", TimeUnit.SECONDS),
    MINUTES("分钟", TimeUnit.MINUTES);

    private String timeUnitStr;
    private TimeUnit timeUnit;

    TimeUnitEnum(String timeUnitStr, TimeUnit timeUnit) {
        this.timeUnitStr = timeUnitStr;
        this.timeUnit = timeUnit;
    }

    public static TimeUnit resolve(String timeUnitStr) {
        if (timeUnitStr == null) {
            return null;
        }
        TimeUnitEnum[] values = TimeUnitEnum.values();
        for (TimeUnitEnum timeUnitEnum : values) {
            if (Objects.equals(timeUnitStr, timeUnitEnum.timeUnitStr)) {
                return timeUnitEnum.timeUnit;
            }
        }
        throw new RuntimeException("don't support this time unit.");
    }

}
