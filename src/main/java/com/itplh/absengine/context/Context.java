package com.itplh.absengine.context;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.AssertUtils;
import lombok.Data;
import org.jsoup.nodes.Element;

@Data
public class Context {

    static final ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();

    /**
     * 当前访问的网页
     */
    private Element element;
    /**
     * 当前操作的脚本
     */
    private Script script;
    /**
     * 当前脚本执行结果
     */
    private Result result;

    private JSONObject other = new JSONObject();

    public String baseUri() {
        return element == null ? null : element.baseUri();
    }

    public static Context initContext(Script script, Element element) {
        AssertUtils.assertNotNull(script, "script is required.");
        Context context = new Context();
        context.setElement(element);
        context.setScript(script);
        Context.contextThreadLocal.set(context);
        return context;
    }

    public static Context getContext() {
        return Context.contextThreadLocal.get();
    }

    public Object put(String key, Object value) {
        return this.other.put(key, value);
    }

    public boolean isSuccess() {
        return this.other.getBooleanValue("success");
    }

}
