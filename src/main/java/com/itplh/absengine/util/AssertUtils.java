package com.itplh.absengine.util;

public class AssertUtils {

    public static void assertNotNull(Object object, String msg) {
        if (object == null) {
            throw new NullPointerException(msg);
        }
    }

    public static void assertNotBlank(String text, String msg) {
        if (StringUtils.isBlank(text)) {
            throw new RuntimeException(msg);
        }
    }

}
