package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.impl.ClickTextScript;
import com.itplh.absengine.script.impl.FormSubmitScript;
import com.itplh.absengine.util.HttpUtils;
import com.itplh.absengine.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

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
    public Result execute() {
        // real time update script
        realtimeUpdate(this);

        log.trace("Line.{} loop:{} {} {}", this.getId(), this.getLoop(), this.getDelayVariable(), this);

        String scriptType = this.getScriptTypeEnum().getValue();
        String clickText = getOperateText();
        Result result = Result.error();
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
            try {
                // do execute
                result = doExecute(context);
                // real time update result, after execute
                this.realtimeUpdate(result);
                log.debug("Line.{} success:{} element:{} loop:{} run:{} {}:{}", this.getId(), result.isSuccess(),
                        result.hasElement(), this.getLoop(), (i + 1), scriptType, clickText);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            // fast fail
            if (result.isError()) {
                break;
            }

            // hooks, after execute
            executeHooks(this, AbstractLifeCycleScript::getAfterExecute);
        }

        return result;
    }

    public abstract Script doPopulate(JSONObject jsonObject);

    public abstract Result doExecute(Context context);

    private String getOperateText() {
        String text;
        switch (this.getScriptTypeEnum()) {
            case 点击文字:
                text = ((ClickTextScript) this).getClickText();
                break;
            case 输入框提交:
                text = ((FormSubmitScript) this).getInputValues().toString();
                break;
            default:
                text = "";
                break;
        }
        return text;
    }

}
