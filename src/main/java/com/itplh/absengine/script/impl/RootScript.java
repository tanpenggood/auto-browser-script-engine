package com.itplh.absengine.script.impl;

import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractRootScript;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.FileUtils;
import com.itplh.absengine.util.JsonUtils;
import com.itplh.absengine.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Data
@Slf4j
public class RootScript extends AbstractRootScript {

    public static RootScript getInstance(String scriptName) {
        if (StringUtils.isBlank(scriptName)) {
            throw new RuntimeException("scriptName is invalid.");
        }
        RootScript root = new RootScript();
        root.setScriptName(scriptName);
        Context context = Context.getContext();
        if (context == null) {
            Context.initContext(root, null);
        }
        return root;
    }

    public static Script load(Script root) {
        if (root instanceof RootScript) {
            LongAdder load = ((RootScript) root).getLoad();
            if (load.intValue() > 0) {
                return root;
            }
            ((RootScript) root).getLoad().increment();
        }
        if (root.getChild() == null || root.getChild().isEmpty()) {
            root.setChild(new ArrayList<>());
        }
        String classpath = FileUtils.buildClasspath(root.getScriptName());
        List<String> allLines = FileUtils.readAllLines(classpath)
                .stream()
                .filter(line -> line.startsWith("{"))
                .collect(Collectors.toList());
        for (String json : allLines) {
            if (JsonUtils.isGlobalVariable(json)) {
                root.populate(json);
                continue;
            }
            Script current = JsonUtils.convert2Script(json, root);
            if (current instanceof ExecuteLocalSetScript) {
                load(current);
            }
            root.getChild().add(current);
        }

        return root;
    }

    public Script load() {
        return load(this);
    }

}
