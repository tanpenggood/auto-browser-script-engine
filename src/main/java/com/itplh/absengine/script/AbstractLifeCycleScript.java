package com.itplh.absengine.script;

import com.itplh.absengine.context.Context;
import lombok.Data;

import java.util.function.Consumer;

@Data
public abstract class AbstractLifeCycleScript extends AbstractHooksScript {

    private Consumer<Context> beforePopulate;
    private Consumer<Context> afterPopulate;
    private Consumer<Context> beforeExecute;
    private Consumer<Context> afterExecute;

}
