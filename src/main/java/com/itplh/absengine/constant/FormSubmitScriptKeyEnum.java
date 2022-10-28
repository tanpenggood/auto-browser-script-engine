package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum FormSubmitScriptKeyEnum {

    多个筛选("多个筛选"),
    按钮文字("按钮文字"),
    输入框("输入框");

    private String value;

    FormSubmitScriptKeyEnum(String value) {
        this.value = value;
    }

}

