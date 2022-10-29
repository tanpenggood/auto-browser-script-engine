package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.annotation.NotNull;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.constant.ScriptTypeEnum;
import com.itplh.absengine.context.Context;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

@Data
@Slf4j
public abstract class AbstractRootScript extends AbstractLifeCycleRootScript {

    /**
     * 循环执行脚本列表的次数
     * <p>
     * 默认值为1，0表示无限循环
     */
    @NotNull
    private long globalLoop = 1L;

    private LongAdder load = new LongAdder();
    private LongAdder execute = new LongAdder();

    @Override
    public Script populate(String json) {
        // real time update script
        realtimeUpdate(this);
        // hooks, before root populate
        executeHooks(script -> getBeforeRootPopulate());

        this.simplePopulate(json, this);
        JSONObject jsonObject = JSON.parseObject(json);
        this.setId(1);
        this.setScriptTypeEnum(ScriptTypeEnum.ROOT);
        // set globalLoop
        Long loop = jsonObject.getLong(CommonScriptKeyEnum.循环次数.getValue());
        loop = loop == null || loop < 0 ? 1 : loop;
        this.setGlobalLoop(loop);

        // hooks, after root populate
        executeHooks(script -> getAfterRootPopulate());

        return this;
    }

    @Override
    public void execute() {
        // check empty script
        if (this.getChild() == null || this.getChild().isEmpty()) {
            return;
        }
        // check repeat execute script
        if (this.getExecute().intValue() > 0) {
            return;
        }
        this.getExecute().increment();

        Context context = getContext();
        // real time update script
        realtimeUpdate(this);
        // hooks, before root execute
        executeHooks(script -> getBeforeRootExecute());
        log.info("{} {} {}", this.getScriptName(), this.getGlobalLoop(), this.getDelayVariable());

        for (long i = 0; i < this.getGlobalLoop() || isForeverLoop(); i++) {
            long start = System.currentTimeMillis();
            // check terminal execute
            if (getTerminalExecute().test(context)) {
                break;
            }
            // hooks, before sub execute
            executeHooks(script -> getBeforeSubExecute());
            // execute sub script list
            this.getChild().forEach(Script::execute);
            // hooks, after sub execute
            executeHooks(script -> getAfterSubExecute());
            // real time update script
            realtimeUpdate(this);
            log.info("No.{} done, cost:{}ms {} {}", (i + 1), (System.currentTimeMillis() - start),
                    this.getScriptName(), this.getGlobalLoop());
        }

        // hooks, after root execute
        executeHooks(script -> getAfterRootExecute());
        // close resource
        remove();
        log.info("{} execute finish. {}", this.getScriptName(), context.baseUri());
    }

    private boolean isForeverLoop() {
        return globalLoop == 0L;
    }

}
