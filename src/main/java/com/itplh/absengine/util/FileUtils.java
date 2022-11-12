package com.itplh.absengine.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Slf4j
public class FileUtils {

    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String SCRIPT_DIRECTORY = "auto-browser-script";

    public static List<String> readAllLinesByPath(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return Collections.EMPTY_LIST;
        }
        try {
            return Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> readAllLinesByPath(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return Collections.EMPTY_LIST;
        }
        return readAllLinesByPath(new File(filePath));
    }

    public static String buildScriptClasspath(String scriptName) {
        if (scriptName == null) {
            return null;
        }
        return CLASSPATH_PREFIX + SCRIPT_DIRECTORY + File.separator + scriptName;
    }

    public static List<String> readAllLinesByClasspath(String classpath) {
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
            File file = Paths.get(uri).toFile();
            return readAllLinesByPath(file);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean write(String text, File file, OpenOption... options) {
        if (text == null || file == null) {
            return false;
        }
        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
        }
        Path writePath = file.toPath();
        try {
            if (Files.exists(writePath)) {
                if (file.isFile()) {
                    Files.write(writePath, text.getBytes(StandardCharsets.UTF_8), options);
                    return true;
                }
            } else {
                // 创建文件并写入
                Files.write(writePath, text.getBytes(StandardCharsets.UTF_8));
                return true;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
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

