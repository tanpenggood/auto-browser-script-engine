package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.ClickTextScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.DelayVariable;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.ElementUtils;
import com.itplh.absengine.util.HttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ClickTextScript extends AbstractCommonScript {

    /**
     * 出现文字
     */
    private String containsText;
    /**
     * 点击文字
     */
    private String clickText;
    /**
     * 在...之后
     */
    private String after;
    /**
     * 在...之前
     */
    private String before;
    /**
     * 多个筛选
     * <p>
     * 默认值1，表示选择第一个匹配项
     */
    private int multipleSelect = 1;
    /**
     * 完全匹配点击
     */
    private boolean equalsClick;
    /**
     * 最低版本(Android)
     */
    private int minVersion;

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String containsText = jsonObject.getString(ClickTextScriptKeyEnum.出现文字.getValue());
        this.setContainsText(containsText);

        String clickText = jsonObject.getString(ClickTextScriptKeyEnum.点击文字.getValue());
        this.setClickText(clickText);

        String after = jsonObject.getString(ClickTextScriptKeyEnum.在之后.getValue());
        this.setAfter(after);

        String before = jsonObject.getString(ClickTextScriptKeyEnum.在之前.getValue());
        this.setBefore(before);

        Integer multipleSelect = jsonObject.getInteger(ClickTextScriptKeyEnum.多个筛选.getValue());
        multipleSelect = multipleSelect == null ? 1 : multipleSelect;
        this.setMultipleSelect(multipleSelect);

        String equalsClick = jsonObject.getString(ClickTextScriptKeyEnum.完全匹配点击.getValue());
        this.setEqualsClick("是".equals(equalsClick));

        Integer minVersion = jsonObject.getIntValue(ClickTextScriptKeyEnum.最低版本.getValue());
        this.setMinVersion(minVersion);

        return this;
    }

    @Override
    public Result doExecute(Context context) {
        DelayVariable delayVariable = this.getDelayVariable();
        return HttpUtils.requestGet(context.baseUri(), delayVariable)
                .flatMap(doc -> {
                    if (equalsClick) {
                        return ElementUtils.selectLinkByEquals(doc, clickText, multipleSelect);
                    }
                    return ElementUtils.selectLinkByLike(doc, clickText, multipleSelect);
                })
                .map(doc -> HttpUtils.clickLink(doc, clickText, delayVariable))
                .map(Result::ok)
                .orElse(Result.error());
    }

}
