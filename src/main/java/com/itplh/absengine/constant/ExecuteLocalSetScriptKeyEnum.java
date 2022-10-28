package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum ExecuteLocalSetScriptKeyEnum {

    本地脚本("本地脚本");

    private String value;

    ExecuteLocalSetScriptKeyEnum(String value) {
        this.value = value;
    }

}
