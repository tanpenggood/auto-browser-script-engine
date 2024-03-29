package com.itplh.absengine.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class LinkAndFormUtils {

    public static String resolveLinkURL(Element element, String linkText) {
        String path = resolveLinkHref(element, linkText);
        if (path.startsWith(HttpUtils.HTTP_SCHEMA)
                || path.startsWith(HttpUtils.HTTPS_SCHEMA)) {
            return path;
        }
        if (path.startsWith(HttpUtils.ABSOLUTE_SCHEMA)) {
            return HttpUtils.HTTP_SCHEMA + path.substring(HttpUtils.ABSOLUTE_SCHEMA.length());
        }
        String baseUri = element.baseUri();
        return resolveURL(baseUri, path);
    }

    public static String resolveFormURL(Element element) {
        String path = resolveFormAction(element);
        String baseUri = element.baseUri();
        return resolveURL(baseUri, path);
    }

    public static String buildGetParameters(Element element, String... value) {
        if (element == null || value == null || value.length == 0) {
            return null;
        }
        // set parameter default value as "", if necessary
        List<String> values = new ArrayList<>(Arrays.asList(value));
        Elements inputs = selectInputs(element);
        int end = inputs.size() - values.size();
        if (end > 0) {
            IntStream.range(0, end).forEach(n -> values.add(""));
        }
        // build parameter
        StringBuilder parameterBuilder = new StringBuilder("?");
        for (int i = 0; i < inputs.size(); i++) {
            Element input = inputs.get(i);
            String name = input.attr("name");
            if (StringUtils.hasText(name)) {
                parameterBuilder.append((name)).append("=").append(values.get(i)).append("&");
            }
        }
        int lastIndex = parameterBuilder.length() - 1;
        if (parameterBuilder.charAt(lastIndex) == '&') {
            parameterBuilder.deleteCharAt(lastIndex);
        }
        return parameterBuilder.toString();
    }

    public static boolean isGetForm(Element element) {
        return HttpUtils.METHOD_GET.equalsIgnoreCase(resolveFormMethod(element));
    }

    public static boolean isPostForm(Element element) {
        return HttpUtils.METHOD_POST.equalsIgnoreCase(resolveFormMethod(element));
    }

    private static String resolveLinkHref(Element element, String text) {
        return ElementUtils.selectFirstLinkByLike(element, text)
                .map(doc -> doc.attr("href"))
                .orElse("");
    }

    private static String resolveFormAction(Element element) {
        return ElementUtils.selectForm(element, 1)
                .map(doc -> doc.attr("action"))
                .orElse("");
    }

    private static String resolveFormMethod(Element element) {
        return ElementUtils.selectForm(element, 1)
                .map(doc -> doc.attr("method"))
                .filter(StringUtils::hasText)
                .orElse(HttpUtils.METHOD_GET);
    }

    private static Elements selectInputs(Element element) {
        if (element == null) {
            return new Elements();
        }
        return ElementUtils.selectForm(element, 1)
                .map(Element::children)
                .map(e -> e.select("input[name]"))
                .orElse(new Elements());
    }

    private static String resolveURL(String baseUri, String path) {
        if (StringUtils.isBlank(baseUri) || StringUtils.isBlank(path)) {
            return null;
        }
        if (baseUri.startsWith(HttpUtils.HTTP_SCHEMA)) {
            baseUri = baseUri.substring(HttpUtils.HTTP_SCHEMA.length());
            baseUri = HttpUtils.HTTP_SCHEMA + baseUri.substring(0, baseUri.indexOf("/"));
        }
        if (baseUri.startsWith(HttpUtils.HTTPS_SCHEMA)) {
            baseUri = baseUri.substring(HttpUtils.HTTPS_SCHEMA.length());
            baseUri = HttpUtils.HTTPS_SCHEMA + baseUri.substring(0, baseUri.indexOf("/"));
        }
        path = path.startsWith("/") ? path : "/" + path;

        return baseUri + path;
    }

}
