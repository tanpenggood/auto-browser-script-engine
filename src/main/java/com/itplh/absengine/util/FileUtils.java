package com.itplh.absengine.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String SCRIPT_DIRECTORY = "auto-browser-script";

    public static String buildScriptClasspath(String scriptName) {
        if (scriptName == null) {
            return null;
        }
        return CLASSPATH_PREFIX + SCRIPT_DIRECTORY + File.separator + scriptName;
    }

    public static List<String> readAllLines(String classpath) {
        List<String> allLines = Collections.EMPTY_LIST;
        if (classpath == null) {
            return allLines;
        }

        URL url = resolveURL(classpath);
        if (url == null) {
            return allLines;
        }

        try {
            URI uri = url.toURI();
            allLines = Files.readAllLines(Paths.get(uri), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
        return allLines;
    }

    private static URL resolveURL(String classpath) {
        if (!classpath.startsWith(CLASSPATH_PREFIX)) {
            throw new RuntimeException("please set valid classpath, such as: classpath:auto-browser-script/sample.zds");
        }

        classpath = classpath.substring(CLASSPATH_PREFIX.length());
        classpath = classpath.startsWith("/") ? classpath.substring(1) : classpath;

        ClassLoader classLoader = ClassLoaderUtils.getClassLoader();
        return classLoader.getResource(classpath);
    }

}

