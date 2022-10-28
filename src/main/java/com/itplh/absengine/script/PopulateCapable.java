package com.itplh.absengine.script;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.CommonScriptKeyEnum;
import com.itplh.absengine.constant.ScriptTypeEnum;
import com.itplh.absengine.constant.TimeUnitEnum;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface PopulateCapable {

    Script populate(String json);

    default Script simplePopulate(String json, Script script) {
        JSONObject jsonObject = JSON.parseObject(json);
        // set script type
        String scriptType = jsonObject.getString(CommonScriptKeyEnum.脚本类型.getValue());
        script.setScriptTypeEnum(ScriptTypeEnum.resolve(scriptType));
        // set delay
        Long delay = jsonObject.getLong(CommonScriptKeyEnum.执行延迟.getValue());
        if (delay == null) {
            delay = Optional.ofNullable(script.getParent())
                    .map(Script::getDelayVariable)
                    .map(DelayVariable::getDelay)
                    .orElse(1500L);
        }
        script.getDelayVariable().setDelay(delay);
        // set delay time unit
        String timeUnitStr = jsonObject.getString(CommonScriptKeyEnum.时间单位.getValue());
        TimeUnit delayTimeUnit = TimeUnitEnum.resolve(timeUnitStr);
        if (timeUnitStr == null) {
            delayTimeUnit = Optional.ofNullable(script.getParent())
                    .map(Script::getDelayVariable)
                    .map(DelayVariable::getDelayTimeUnit)
                    .orElse(null);
        }
        script.getDelayVariable().setDelayTimeUnit(delayTimeUnit);

        return script;
    }

}
