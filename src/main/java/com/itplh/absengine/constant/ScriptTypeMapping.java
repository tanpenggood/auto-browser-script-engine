package com.itplh.absengine.constant;

import com.itplh.absengine.script.Script;
import com.itplh.absengine.script.impl.ClickTextScript;
import com.itplh.absengine.script.impl.ContainsTextLogicScript;
import com.itplh.absengine.script.impl.DelayScript;
import com.itplh.absengine.script.impl.ExecuteLocalSetScript;
import com.itplh.absengine.script.impl.FormSubmitScript;
import com.itplh.absengine.script.impl.OpenURLScript;
import com.itplh.absengine.script.impl.PauseScript;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ScriptTypeMapping {

    点击文字(ScriptTypeEnum.点击文字.getValue(), ClickTextScript.class),
    输入框提交(ScriptTypeEnum.输入框提交.getValue(), FormSubmitScript.class),
    进入网址(ScriptTypeEnum.进入网址.getValue(), OpenURLScript.class),
    执行本地脚本集(ScriptTypeEnum.执行本地脚本集.getValue(), ExecuteLocalSetScript.class),
    逻辑脚本_出现文字(ScriptTypeEnum.逻辑脚本_出现文字.getValue(), ContainsTextLogicScript.class),
    脚本暂停(ScriptTypeEnum.脚本暂停.getValue(), PauseScript.class),
    延时脚本(ScriptTypeEnum.延时脚本.getValue(), DelayScript.class);

    private String type;
    private Class<? extends Script> clazz;

    ScriptTypeMapping(String type, Class<? extends Script> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static Class<? extends Script> resolveClass(Object type) {
        if (type == null) {
            return null;
        }
        for (ScriptTypeMapping mapping : ScriptTypeMapping.values()) {
            if (Objects.equals(type, mapping.getType())) {
                return mapping.getClazz();
            }
        }
        throw new RuntimeException("don't support this type: " + type);
    }

}

