package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum ClickTextScriptKeyEnum {

    出现文字("出现文字"),
    点击文字("点击文字"),
    在之后("在...之后"),
    在之前("在...之前"),
    多个筛选("多个筛选"),
    完全匹配点击("完全匹配点击"),
    最低版本("最低版本(Android)");

    private String value;

    ClickTextScriptKeyEnum(String value) {
        this.value = value;
    }

}

