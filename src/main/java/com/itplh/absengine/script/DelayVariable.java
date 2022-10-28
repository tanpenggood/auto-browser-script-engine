package com.itplh.absengine.script;

import com.itplh.absengine.annotation.Nullable;
import com.itplh.absengine.util.DelayUtils;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class DelayVariable {

    /**
     * 脚本执行前的等候时间
     * <p>
     * 默认值为1500毫秒，每个脚本都可以设置一个单独的延迟
     */
    @Nullable
    private long delay = DelayUtils.DEFAULT_DELAY;
    /**
     * 时间单位
     * <p>
     * 默认值为毫秒
     */
    @Nullable
    private TimeUnit delayTimeUnit = DelayUtils.DEFAULT_DELAY_TIME_UNIT;

}
