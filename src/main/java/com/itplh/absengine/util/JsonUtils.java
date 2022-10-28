package com.itplh.absengine.util;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.itplh.absengine.constant.ScriptTypeEnum;
import com.itplh.absengine.constant.ScriptTypeMapping;
import com.itplh.absengine.script.Script;

public class JsonUtils {

    // 缓存起来重复使用能提升性能
    public static final JSONPath scriptTypeExpression = JSONPath.of("$.脚本类型");

    public static Script convert2Script(String json, Script parent) {
        if (json == null || parent == null) {
            return null;
        }
        JSONReader parser = JSONReader.of(json);
        Object scriptType = scriptTypeExpression.extract(parser);
        Class<? extends Script> scriptClazz = ScriptTypeMapping.resolveClass(scriptType);
        Script script;
        try {
            script = scriptClazz.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call construct.");
        }
        script.setParent(parent);
        script.setId(parent.getChild().size() + 2);
        script.populate(json);
        return script;
    }

    public static boolean isGlobalVariable(String json) {
        JSONReader parser = JSONReader.of(json);
        Object scriptType = scriptTypeExpression.extract(parser);
        return ScriptTypeEnum.全局变量.getValue().equals(scriptType);
    }

}
