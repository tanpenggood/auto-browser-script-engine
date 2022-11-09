package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.annotation.NotNull;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.constant.ScriptTypeEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.util.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

@Data
@Slf4j
public abstract class AbstractRootScript extends AbstractLifeCycleRootScript implements ConsumerHooksHelper {

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
        executeRootHooks(this, AbstractLifeCycleRootScript::getBeforeRootPopulate);

        this.simplePopulate(json, this);
        JSONObject jsonObject = JSON.parseObject(json);
        this.setId(1);
        this.setScriptTypeEnum(ScriptTypeEnum.ROOT);
        // set globalLoop
        Long loop = jsonObject.getLong(CommonScriptKeyEnum.循环次数.getValue());
        loop = loop == null || loop < 0 ? 1 : loop;
        this.setGlobalLoop(loop);

        // hooks, after root populate
        executeRootHooks(this, AbstractLifeCycleRootScript::getAfterRootPopulate);

        return this;
    }

    @Override
    public Result execute() {
        // check empty script
        if (CollectionUtils.isEmpty(this.getChild())) {
            return Result.ok();
        }
        // check repeat execute script
        if (this.getExecute().intValue() > 0) {
            return Result.ok();
        }
        this.getExecute().increment();

        Context context = getContext();
        // real time update script
        realtimeUpdate(this);
        // hooks, before root execute
        executeRootHooks(this, AbstractLifeCycleRootScript::getBeforeRootExecute);
        log.info("{} loop:{} script:{} {}", this.getScriptName(), this.getGlobalLoop(),
                this.getScriptTypeEnum().getValue(), this.getDelayVariable());

        boolean isTerminal = false;
        boolean isTerminalFromSub = false;
        Result result = Result.error();
        for (long i = 0; i < this.getGlobalLoop() || isForeverLoop(); i++) {
            long start = System.currentTimeMillis();
            // hooks, before sub execute
            executeRootHooks(this, AbstractLifeCycleRootScript::getBeforeSubExecute);
            // execute sub script list
            for (Script script : this.getChild()) {
                result = script.execute();
                // check sub script terminal execute
                isTerminalFromSub = getTerminalFromSub().test(context);
                if (isTerminalFromSub) {
                    break;
                }
            }
            // hooks, after sub execute
            executeRootHooks(this, AbstractLifeCycleRootScript::getAfterSubExecute);
            // real time update script
            realtimeUpdate(this);
            log.info("No.{} {} done, cost:{}ms", (i + 1), this.getScriptName(),
                    (System.currentTimeMillis() - start));
            // check root script terminal execute or sub script terminal execute
            isTerminal = getTerminal().test(context);
            if (isTerminal || isTerminalFromSub) {
                break;
            }
        }

        // hooks, after root execute
        executeRootHooks(this, AbstractLifeCycleRootScript::getAfterRootExecute);
        log.info("{} execute finish. {}", this.getScriptName(), context.baseUri());
        // close resource
        remove();
        return result;
    }

    private boolean isForeverLoop() {
        return globalLoop == 0L;
    }

}
