package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum ContainsTextLogicScriptKeyEnum {

    出现文字("出现文字"),
    出现文字则执行("出现文字,则执行:"),
    未出现文字则执行("未出现文字,则执行:");

    private String value;

    ContainsTextLogicScriptKeyEnum(String value) {
        this.value = value;
    }

}

