package com.itplh.absengine.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;
import java.util.stream.Collectors;

public class LinkAndFormUtils {

    public static String resolveLinkURL(Element element, String linkText) {
        String path = resolveLinkHref(element, linkText);
        return resolveURL(element, path);
    }

    public static String resolveFormURL(Element element) {
        String path = resolveFormAction(element);
        return resolveURL(element, path);
    }

    public static String buildGetParameters(Element element, String... value) {
        if (element == null || value == null || value.length == 0) {
            return null;
        }
        StringBuilder parameterBuilder = new StringBuilder("?");
        Elements inputs = selectInputs(element);
        for (int i = 0; i < inputs.size(); i++) {
            Element input = inputs.get(i);
            String name = input.attr("name");
            parameterBuilder.append((name)).append("=").append(value[i]).append("&");
        }
        parameterBuilder.deleteCharAt(parameterBuilder.lastIndexOf("&"));
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
        return Optional.ofNullable(element.selectFirst("form[action]"))
                .map(doc -> doc.attr("action"))
                .orElse("");
    }

    private static String resolveFormMethod(Element element) {
        return Optional.ofNullable(element.selectFirst("form[method]"))
                .map(doc -> doc.attr("method"))
                .orElse(HttpUtils.METHOD_GET);
    }

    private static Elements selectInputs(Element element) {
        if (element == null) {
            return new Elements();
        }
        return element.selectFirst("form")
                .children()
                .stream()
                .filter(e -> e.is("input[name]"))
                .collect(Collectors.toCollection(Elements::new));
    }

    private static String resolveURL(Element element, String path) {
        if (element == null || StringUtils.isBlank(path)) {
            return null;
        }

        String url = element.baseUri();
        if (url.startsWith(HttpUtils.HTTP_SCHEMA)) {
            url = url.substring(HttpUtils.HTTP_SCHEMA.length());
            url = HttpUtils.HTTP_SCHEMA + url.substring(0, url.indexOf("/"));
        }
        if (url.startsWith(HttpUtils.HTTPS_SCHEMA)) {
            url = url.substring(HttpUtils.HTTPS_SCHEMA.length());
            url = HttpUtils.HTTPS_SCHEMA + url.substring(0, url.indexOf("/"));
        }
        path = path.startsWith("/") ? path : "/" + path;
        url += path;

        return url;
    }

}
