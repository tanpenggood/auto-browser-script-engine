package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.OpenURLScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.DelayVariable;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.HttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class OpenURLScript extends AbstractCommonScript {

    /**
     * 进入网址
     */
    private String url;

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String url = jsonObject.getString(OpenURLScriptKeyEnum.进入网址.getValue());
        this.setUrl(url);

        return this;
    }

    @Override
    public Result doExecute(Context context) {
        DelayVariable delayVariable = this.getDelayVariable();
        return HttpUtils.requestGet(url, delayVariable)
                .map(Result::ok)
                .orElse(Result.error());
    }

}
