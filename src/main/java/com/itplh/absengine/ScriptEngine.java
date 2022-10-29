package com.itplh.absengine;

import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.script.impl.RootScript;
import com.itplh.absengine.util.HttpUtils;
import org.jsoup.nodes.Document;

public class ScriptEngine {

    public static RootScript buildRoot(String scriptName) {
        return RootScript.getInstance(scriptName);
    }

    public static RootScript buildRoot(String scriptName, String url) {
        RootScript root = RootScript.getInstance(scriptName);
        Document document = HttpUtils.requestGet(url, null).orElse(null);
        Context.initContext(root, document);
        return root;
    }

    public static Script load(Script root) {
        return RootScript.load(root);
    }

}
