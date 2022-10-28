package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum OpenURLScriptKeyEnum {

    进入网址("进入网址");

    private String value;

    OpenURLScriptKeyEnum(String value) {
        this.value = value;
    }

}
