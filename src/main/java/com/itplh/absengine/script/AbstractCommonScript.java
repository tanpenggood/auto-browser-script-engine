package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class AbstractCommonScript extends AbstractLifeCycleScript {

    @Override
    public Script populate(String json) {
        // real time update script
        realtimeUpdate(this);
        // hooks, before populate
        executeHooks(script -> getBeforePopulate());

        this.simplePopulate(json, this);
        JSONObject jsonObject = JSON.parseObject(json);
        // set loop
        Long loop = jsonObject.getLong(CommonScriptKeyEnum.重复执行次数.getValue());
        loop = loop == null ? 1 : loop;
        loop = loop < 1 || loop > 999_999_999L ? 1 : loop;
        this.setLoop(loop);

        doPopulate(jsonObject);

        // hooks, after populate
        executeHooks(script -> getAfterPopulate());

        return this;
    }

    @Override
    public void execute() {
        // real time update script
        realtimeUpdate(this);

        log.trace("Line.{} loop:{} {} {}", this.getId(), this.getLoop(), this.getDelayVariable(), this);

        for (int i = 0; i < this.getLoop(); i++) {
            Context context = getContext();
            // hooks, before execute
            executeHooks(script -> getBeforeExecute());
            // do execute
            Result result = Result.error();
            try {
                result = doExecute(context);
                log.debug("Line.{} success:{} has element:{} loop:{} {}",
                        this.getId(), result.isSuccess(), result.hasElement(),
                        this.getLoop(), this.getDelayVariable());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            // fast fail
            if (result.isError()) {
                break;
            }
            // real time update element
            Optional.ofNullable(result.getElement()).ifPresent(this::realtimeUpdate);
            // hooks, after execute
            executeHooks(script -> getAfterExecute());
        }
    }

    public abstract Script doPopulate(JSONObject jsonObject);

    public abstract Result doExecute(Context context);

}
