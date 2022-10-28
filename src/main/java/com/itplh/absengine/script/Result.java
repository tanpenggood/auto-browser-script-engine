package com.itplh.absengine.script;

import lombok.Data;
import org.jsoup.nodes.Element;

@Data
public class Result {

    private String code;
    private String msg;
    private boolean success;
    private Element element;
    private long timestamp = System.currentTimeMillis();

    public Result(String code, String msg, boolean success, Element element) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.element = element;
    }

    public static Result ok(Element element) {
        return new Result("1", "执行成功", true, element);
    }

    public static Result ok() {
        return ok(null);
    }

    public static Result error() {
        return new Result("0", "执行失败", false, null);
    }

    public boolean isError() {
        return !success;
    }

    public boolean hasElement() {
        return success && this.element != null;
    }

}
