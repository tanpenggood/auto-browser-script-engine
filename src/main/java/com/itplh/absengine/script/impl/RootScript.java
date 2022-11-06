package com.itplh.absengine.script.impl;

import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractRootScript;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.AssertUtils;
import com.itplh.absengine.util.CollectionUtils;
import com.itplh.absengine.util.FileUtils;
import com.itplh.absengine.util.JsonUtils;
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
        AssertUtils.assertNotBlank(scriptName, "script name is required.");
        RootScript root = new RootScript();
        root.setScriptName(scriptName);
        Context context = Context.getContext();
        if (context == null) {
            Context.initContext(root, null);
        }
        return root;
    }

    public static Script load(Script root) {
        AssertUtils.assertNotNull(root, "root is required.");
        AssertUtils.assertNotBlank(root.getScriptName(), "script name is required.");
        if (root instanceof RootScript) {
            LongAdder load = ((RootScript) root).getLoad();
            if (load.intValue() > 0) {
                return root;
            }
            ((RootScript) root).getLoad().increment();
        }
        doLoad(root);

        return root;
    }

    public Script load() {
        return load(this);
    }

    private static void doLoad(Script root) {
        if (CollectionUtils.isEmpty(root.getChild())) {
            root.setChild(new ArrayList<>());
        }
        String classpath = FileUtils.buildClasspath(root.getScriptName());
        List<String> allLines = FileUtils.readAllLines(classpath)
                .stream()
                .filter(line -> line.startsWith("{"))
                .collect(Collectors.toList());
        for (String json : allLines) {
            if (JsonUtils.isGlobalVariable(json)) {
                if (root instanceof RootScript) {
                    root.populate(json);
                }
                continue;
            }
            Script current = JsonUtils.convert2Script(json, root);
            root.getChild().add(current);
        }
    }

}
