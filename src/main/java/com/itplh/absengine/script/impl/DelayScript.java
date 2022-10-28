package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.DelayUtils;
import lombok.Data;

@Data
public class DelayScript extends AbstractCommonScript {

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        return this;
    }

    @Override
    public Result doExecute(Context context) {
        DelayUtils.delay(this.getDelayVariable());
        return Result.ok();
    }

}
