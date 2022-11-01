package com.itplh.absengine.script;

import com.itplh.absengine.context.Context;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ConsumerHooksHelper {

    /**
     * Find from this to parent until find the hooks implement
     */
    default void executeHooks(Script script, Function<AbstractLifeCycleScript, Consumer<Context>> function) {
        if (script == null || function == null) {
            return;
        }
        Consumer<Context> consumer = null;
        while (consumer == null && script != null) {
            consumer = function.apply((AbstractLifeCycleScript) script);
            script = script.getParent();
        }
        if (consumer == null) {
            return;
        }
        consumer.accept(Context.getContext());
    }

    /**
     * Execute root hooks
     */
    default void executeRootHooks(Script script, Function<AbstractLifeCycleRootScript, Consumer<Context>> function) {
        if (script == null || function == null) {
            return;
        }
        Consumer<Context> consumer = function.apply((AbstractLifeCycleRootScript) script);
        if (consumer == null) {
            return;
        }
        consumer.accept(Context.getContext());
    }

}
