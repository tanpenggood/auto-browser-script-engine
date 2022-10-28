package com.itplh.absengine.context;

import com.itplh.absengine.script.Script;
import org.jsoup.nodes.Element;

public interface ContextAware {

    default Context initContext(Script script, Element element) {
        return Context.initContext(script, element);
    }

    default Context getContext() {
        return Context.getContext();
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
