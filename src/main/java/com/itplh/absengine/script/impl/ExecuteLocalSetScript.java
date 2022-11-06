package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.ExecuteLocalSetScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ExecuteLocalSetScript extends AbstractCommonScript {

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String scriptName = jsonObject.getString(ExecuteLocalSetScriptKeyEnum.本地脚本.getValue());
        this.setScriptName(scriptName);
        RootScript.load(this);
        return this;
    }

    @Override
    public Result doExecute(Context context) {
        log.debug("Line.{} loop:{} script:{} {} {}", this.getId(), this.getLoop(),
                this.getScriptTypeEnum().getValue(), this.getScriptName(), this.getDelayVariable());
        // check empty script
        if (CollectionUtils.isEmpty(this.getChild())) {
            return Result.ok();
        }
        // execute sub script list
        Result result = Result.error();
        for (Script script : this.getChild()) {
            result = script.execute();
        }
        return result;
    }

}
