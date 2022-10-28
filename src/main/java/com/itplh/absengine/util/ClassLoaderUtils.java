package com.itplh.absengine.util;

public class ClassLoaderUtils {

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = ClassLoaderUtils.class.getClassLoader();
        classLoader = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
        return classLoader;
    }

}

