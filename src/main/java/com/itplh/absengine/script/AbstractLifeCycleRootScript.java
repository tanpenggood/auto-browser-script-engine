package com.itplh.absengine.script;

import com.itplh.absengine.context.Context;
import lombok.Data;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Data
public abstract class AbstractLifeCycleRootScript extends AbstractLifeCycleScript {

    /**
     * root脚本填充属性之前
     */
    private Consumer<Context> beforeRootPopulate;
    /**
     * root脚本填充属性之后
     */
    private Consumer<Context> afterRootPopulate;
    /**
     * root脚本执行之前
     */
    private Consumer<Context> beforeRootExecute;
    /**
     * root脚本执行之后
     */
    private Consumer<Context> afterRootExecute;
    /**
     * root执行子脚本之前
     */
    private Consumer<Context> beforeSubExecute;
    /**
     * root执行子脚本之后
     */
    private Consumer<Context> afterSubExecute;
    /**
     * 终止脚本执行
     */
    private Predicate<Context> terminal = (ctx) -> false;
    /**
     * 终止脚本执行（从子脚本发出）
     */
    private Predicate<Context> terminalFromSub = (ctx) -> false;

}
