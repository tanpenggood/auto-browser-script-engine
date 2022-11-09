package com.itplh.absengine.context;

import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.AssertUtils;
import org.jsoup.nodes.Element;

import java.util.Optional;

public interface ContextAware {

    default Context initContext(Script script, Element element) {
        return Context.initContext(script, element);
    }

    default Context getContext() {
        return Context.getContext();
    }

    default Context realtimeUpdate(Result result) {
        AssertUtils.assertNotNull(result, "result is required.");
        Context context = getContext();
        Optional.ofNullable(result.getElement()).ifPresent(this::realtimeUpdate);
        context.put("success", result.isSuccess());
        return context;
    }

    default Context realtimeUpdate(Element element) {
        Context context = getContext();
        context.setElement(element);
        return context;
    }

    default Context realtimeUpdate(Script script) {
        Context context = getContext();
        context.setScript(script);
        return context;
    }

    default void remove() {
        Context.contextThreadLocal.remove();
    }

}
