package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.ExecuteLocalSetScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import lombok.Data;

@Data
public class ExecuteLocalSetScript extends AbstractCommonScript {

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String scriptName = jsonObject.getString(ExecuteLocalSetScriptKeyEnum.本地脚本.getValue());
        this.setScriptName(scriptName);
        return this;
    }

    @Override
    public Result doExecute(Context context) {
        return Result.ok();
    }

}
