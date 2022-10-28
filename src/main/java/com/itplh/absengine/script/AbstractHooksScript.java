package com.itplh.absengine.script;

import com.itplh.absengine.context.Context;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractHooksScript extends Script {

    /**
     * Find from this to parent until find the hooks implement
     */
    protected void executeHooks(Function<Script, Consumer<Context>> supplier) {
        if (supplier == null) {
            return;
        }
        Script script = this;
        Consumer<Context> consumer;
        do {
            consumer = supplier.apply(script);
            script = script.getParent();
        } while (consumer == null && script != null);
        if (consumer == null) {
            return;
        }
        consumer.accept(getContext());
    }

}
