package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.ContainsTextLogicScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.JsonUtils;
import lombok.Data;

@Data
public class ContainsTextLogicScript extends AbstractCommonScript {

    /**
     * 出现文字
     */
    private String containsText;
    /**
     * 出现文字,则执行:
     */
    private Script containsTextExecute;
    /**
     * 未出现文字,则执行:
     */
    private Script notContainsTextExecute;

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String containsText = jsonObject.getString(ContainsTextLogicScriptKeyEnum.出现文字.getValue());
        this.setContainsText(containsText);

        String containsTextExecuteJson = jsonObject.getString(ContainsTextLogicScriptKeyEnum.出现文字则执行.getValue());
        Script containsTextExecute = JsonUtils.convert2Script(containsTextExecuteJson, this);
        this.setContainsTextExecute(containsTextExecute);

        String notContainsTextExecuteJson = jsonObject.getString(ContainsTextLogicScriptKeyEnum.未出现文字则执行.getValue());
        Script notContainsTextExecute = JsonUtils.convert2Script(notContainsTextExecuteJson, this);
        this.setNotContainsTextExecute(notContainsTextExecute);

        return this;
    }


    @Override
    public Result doExecute(Context context) {
        return Result.ok();
    }

}
