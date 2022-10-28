package com.itplh.absengine;

import com.itplh.absengine.script.Script;
import com.itplh.absengine.script.impl.RootScript;

public class ScriptEngine {

    public static RootScript buildRootScript(String scriptName) {
        return RootScript.getInstance(scriptName);
    }

    public static Script load(Script root) {
        return RootScript.load(root);
    }

}
