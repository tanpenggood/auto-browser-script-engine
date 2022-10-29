package com.itplh.absengine.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ElementUtils {

    public static Elements selectLinkByLike(Element element, String text) {
        if (element == null || StringUtils.isBlank(text)) {
            return new Elements();
        }
        String cssQuery = String.format("a:containsOwn(%s)", text);
        return element.select(cssQuery);
    }

    /**
     * @param element
     * @param text
     * @param select  当检索内容里出现多个匹配元素时，用此参数选择第几个，0表示随机选择一个，负数表示倒数第几个
     * @return
     */
    public static Optional<Element> selectLinkByLike(Element element, String text, int select) {
        return Optional.ofNullable(selectLinkByLike(element, text))
                .filter(CollectionUtils::isNotEmpty)
                .map(els -> {
                    int index = resolveIndex(els.size(), select);
                    return els.get(index);
                });
    }

    public static Optional<Element> selectFirstLinkByLike(Element element, String text) {
        return selectLinkByLike(element, text, 1);
    }

    public static Optional<Element> selectLinkByEquals(Element element, String text, int select) {
        Elements elements = selectLinkByLike(element, text)
                .stream()
                .filter(e -> Objects.equals(e.ownText(), text))
                .collect(Collectors.toCollection(Elements::new));
        return Optional.ofNullable(elements)
                .filter(CollectionUtils::isNotEmpty)
                .map(els -> {
                    int index = resolveIndex(els.size(), select);
                    return els.get(index);
                });
    }

    public static Optional<Element> selectFirstLinkByEquals(Element element, String text) {
        return selectLinkByLike(element, text)
                .stream()
                .filter(e -> Objects.equals(text, e.ownText()))
                .findFirst();
    }

    /**
     * @param element
     * @param select  当检索内容里出现多个匹配元素时，用此参数选择第几个，0表示随机选择一个，负数表示倒数第几个
     * @return
     */
    public static Optional<Element> selectForm(Element element, int select) {
        if (element == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(element.select("form[action]"))
                .filter(CollectionUtils::isNotEmpty)
                .map(els -> {
                    int index = resolveIndex(els.size(), select);
                    return els.get(index);
                });
    }

    private static int resolveIndex(int elementsSize, int select) {
        if (elementsSize < 1) {
            throw new RuntimeException("elementsSize is invalid, elementsSize: " + elementsSize);
        }
        int index = 0;
        if (Math.abs(select) > elementsSize) {
            index = 0;
        }
        if (select == 0) {
            index = ThreadLocalRandom.current().nextInt(elementsSize);
        }
        if (select > 0) {
            index = select - 1;
        }
        if (select < 0) {
            index = elementsSize + select;
        }
        return index;
    }

}
