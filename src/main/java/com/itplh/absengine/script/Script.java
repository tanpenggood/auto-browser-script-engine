package com.itplh.absengine.script;

import com.alibaba.fastjson2.annotation.JSONField;
import com.itplh.absengine.annotation.NotNull;
import com.itplh.absengine.annotation.Nullable;
import com.itplh.absengine.constant.ScriptTypeEnum;
import com.itplh.absengine.context.ContextAware;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public abstract class Script implements ContextAware, PopulateCapable, ExecuteCapable {

    private int id;
    @JSONField(serialize = false)
    private Script parent;
    private List<Script> child = Collections.EMPTY_LIST;

    /**
     * 脚本类型
     */
    @NotNull
    private ScriptTypeEnum scriptTypeEnum;
    /**
     * 脚本名称
     */
    @Nullable
    private String scriptName;
    /**
     * 延时变量
     */
    @Nullable
    private DelayVariable delayVariable = new DelayVariable();
    /**
     * 重复执行次数
     * <p>
     * 默认值为1，最大值为999,999,999
     */
    @NotNull
    private long loop = 1L;

}
