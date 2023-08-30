package com.itplh.absengine.script;

import com.itplh.absengine.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class DelayVariable {

    public static final long DEFAULT_DELAY = 1500L;

    public static final TimeUnit DEFAULT_DELAY_TIME_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 脚本执行前的等候时间
     * <p>
     * 默认值为1500毫秒，每个脚本都可以设置一个单独的延迟
     */
    @Nullable
    private long delay = DEFAULT_DELAY;
    /**
     * 时间单位
     * <p>
     * 默认值为毫秒
     */
    @Nullable
    private TimeUnit delayTimeUnit = DEFAULT_DELAY_TIME_UNIT;

    public DelayVariable(long delay, TimeUnit delayTimeUnit) {
        this.delay = delay;
        this.delayTimeUnit = delayTimeUnit;
    }

    public static DelayVariable defaultDelay() {
        return new DelayVariable();
    }

    public static DelayVariable buildSeconds(long delay) {
        return new DelayVariable(delay, TimeUnit.SECONDS);
    }

}
