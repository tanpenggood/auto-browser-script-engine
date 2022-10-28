package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.FormSubmitScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.DelayVariable;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.HttpUtils;
import com.itplh.absengine.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormSubmitScript extends AbstractCommonScript {

    /**
     * 多个筛选
     * <p>
     * 默认值1，表示选择第一个form表单
     */
    private int multipleSelect = 1;
    /**
     * 按钮文字
     */
    private String buttonText;
    /**
     * 输入值
     */
    private List<String> inputValues;

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        Integer multipleSelect = jsonObject.getInteger(FormSubmitScriptKeyEnum.多个筛选.getValue());
        multipleSelect = multipleSelect == null ? 1 : multipleSelect;
        this.setMultipleSelect(multipleSelect);

        String buttonText = jsonObject.getString(FormSubmitScriptKeyEnum.按钮文字.getValue());
        this.setButtonText(buttonText);

        inputValues = new ArrayList<>();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String inputValue = jsonObject.getString(FormSubmitScriptKeyEnum.输入框.getValue() + i);
            if (StringUtils.isBlank(inputValue)) {
                break;
            }
            inputValues.add(inputValue);
        }

        return this;
    }

    @Override
    public Result doExecute(Context context) {
        DelayVariable delayVariable = this.getDelayVariable();
        return HttpUtils.requestGet(context.baseUri(), delayVariable)
                .map(doc -> HttpUtils.formSubmit(doc, delayVariable, inputValues.toArray(new String[0])))
                .map(Result::ok)
                .orElse(Result.error());
    }

}
