package com.itplh.absengine.constant;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ScriptTypeEnum {

    ROOT("根节点"),
    全局变量("全局变量"),
    点击文字("点击文字"),
    输入框提交("输入框提交"),
    进入网址("进入网址"),
    执行本地脚本集("执行本地脚本集"),
    逻辑脚本_出现文字("逻辑脚本-出现文字"),
    脚本暂停("脚本暂停"),
    延时脚本("延时脚本");

    private String value;

    ScriptTypeEnum(String value) {
        this.value = value;
    }

    public static ScriptTypeEnum resolve(String type) {
        if (type == null) {
            return null;
        }
        ScriptTypeEnum[] values = ScriptTypeEnum.values();
        for (ScriptTypeEnum scriptTypeEnum : values) {
            if (Objects.equals(type, scriptTypeEnum.value)) {
                return scriptTypeEnum;
            }
        }
        throw new RuntimeException("this type isn't supported");
    }

}

