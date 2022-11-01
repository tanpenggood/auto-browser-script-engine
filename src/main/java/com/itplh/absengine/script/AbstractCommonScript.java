package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.util.HttpUtils;
import com.itplh.absengine.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.Optional;

@Slf4j
public abstract class AbstractCommonScript extends AbstractLifeCycleScript implements ConsumerHooksHelper {

    @Override
    public Script populate(String json) {
        // real time update script
        realtimeUpdate(this);
        // hooks, before populate
        executeHooks(this, AbstractLifeCycleScript::getBeforePopulate);

        this.simplePopulate(json, this);
        JSONObject jsonObject = JSON.parseObject(json);
        // set loop
        Long loop = jsonObject.getLong(CommonScriptKeyEnum.重复执行次数.getValue());
        loop = loop == null ? 1 : loop;
        loop = loop < 1 || loop > 999_999_999L ? 1 : loop;
        this.setLoop(loop);

        doPopulate(jsonObject);

        // hooks, after populate
        executeHooks(this, AbstractLifeCycleScript::getAfterPopulate);

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
            executeHooks(this, AbstractLifeCycleScript::getBeforeExecute);
            // real time update operate element, before execute
            Element current = context.getElement();
            if (current == null && StringUtils.hasText(context.baseUri())) {
                current = HttpUtils.requestGet(context.baseUri(), this.getDelayVariable()).orElse(null);
                realtimeUpdate(current);
            }
            // do execute
            Result result = Result.error();
            try {

                result = doExecute(context);
                log.debug("Line.{} success:{} element:{} loop:{} script:{} {}",
                        this.getId(), result.isSuccess(), result.hasElement(), this.getLoop(),
                        this.getScriptTypeEnum().getValue(), this.getDelayVariable());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            // fast fail
            if (result.isError()) {
                break;
            }
            // real time update operate element, after execute
            Optional.ofNullable(result.getElement()).ifPresent(this::realtimeUpdate);
            // hooks, after execute
            executeHooks(this, AbstractLifeCycleScript::getAfterExecute);
        }
    }

    public abstract Script doPopulate(JSONObject jsonObject);

    public abstract Result doExecute(Context context);

}
