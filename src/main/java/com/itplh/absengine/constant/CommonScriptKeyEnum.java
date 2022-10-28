package com.itplh.absengine.constant;

import lombok.Getter;

@Getter
public enum CommonScriptKeyEnum {

    脚本类型("脚本类型"),
    循环次数("循环次数"),
    重复执行次数("重复执行次数"),
    限制执行次数("限制执行次数"),
    执行延迟("执行延迟"),
    时间单位("时间单位");

    private String value;

    CommonScriptKeyEnum(String value) {
        this.value = value;
    }

}

