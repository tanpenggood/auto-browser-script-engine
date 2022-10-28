package com.itplh.absengine.util;

public class StringUtils {

    public static boolean isEmpty(String text) {
        return text == null || "".equals(text);
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isBlank(String text) {
        return isEmpty(text) || "".equals(text.trim());
    }

    public static boolean hasText(String text) {
        return !isBlank(text);
    }

}
