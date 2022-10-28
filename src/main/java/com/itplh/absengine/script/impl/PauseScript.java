package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import lombok.Data;

@Data
public class PauseScript extends AbstractCommonScript {

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        return null;
    }

    @Override
    public Result doExecute(Context context) {
        return Result.ok();
    }

}
