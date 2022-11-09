package com.itplh.absengine.script.impl;

import com.alibaba.fastjson2.JSONObject;
import com.itplh.absengine.constant.ClickTextScriptKeyEnum;
import com.itplh.absengine.context.Context;
import com.itplh.absengine.script.AbstractCommonScript;
import com.itplh.absengine.script.DelayVariable;
import com.itplh.absengine.script.Result;
import com.itplh.absengine.script.Script;
import com.itplh.absengine.util.ElementUtils;
import com.itplh.absengine.util.HttpUtils;
import com.itplh.absengine.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ClickTextScript extends AbstractCommonScript {

    /**
     * 出现文字
     * <p>
     * 当页面出现此文字时才执行。可以用";"间隔输入多个关键词，页面中出现关键词之一即可执行
     */
    private String containsText;
    /**
     * 点击文字
     * <p>
     * 要点击的链接，可以用";"间隔输入多个关键词，执行最先匹配到的链接
     */
    private String clickText;
    /**
     * 在...之后
     * <p>
     * 在此链接后开始检索，点击文字必须在此之后
     */
    private String after;
    /**
     * 在...之前
     * <p>
     * 检索到此链接为止，点击文字必须在此之前
     */
    private String before;
    /**
     * 多个筛选
     * <p>
     * 默认值1，表示选择第一个匹配项
     */
    private int multipleSelect = 1;
    /**
     * 完全匹配点击
     */
    private boolean equalsClick;
    /**
     * 最低版本(Android)
     */
    private int minVersion;

    @Override
    public Script doPopulate(JSONObject jsonObject) {
        String containsText = jsonObject.getString(ClickTextScriptKeyEnum.出现文字.getValue());
        this.setContainsText(containsText);

        String clickText = jsonObject.getString(ClickTextScriptKeyEnum.点击文字.getValue());
        this.setClickText(clickText);

        String after = jsonObject.getString(ClickTextScriptKeyEnum.在之后.getValue());
        this.setAfter(after);

        String before = jsonObject.getString(ClickTextScriptKeyEnum.在之前.getValue());
        this.setBefore(before);

        Integer multipleSelect = jsonObject.getInteger(ClickTextScriptKeyEnum.多个筛选.getValue());
        multipleSelect = multipleSelect == null ? 1 : multipleSelect;
        this.setMultipleSelect(multipleSelect);

        String equalsClick = jsonObject.getString(ClickTextScriptKeyEnum.完全匹配点击.getValue());
        this.setEqualsClick("是".equals(equalsClick));

        Integer minVersion = jsonObject.getIntValue(ClickTextScriptKeyEnum.最低版本.getValue());
        this.setMinVersion(minVersion);

        return this;
    }

    @Override
    public Result doExecute(Context context) {
        DelayVariable delayVariable = this.getDelayVariable();
        return Optional.ofNullable(context.getElement())
                // containsText
                .filter(doc -> containsTextIfPresent(doc, containsText, false))
                .flatMap(doc -> {
                    // after if present
                    Element element = filterIfAfterPresent(doc, after);
                    // before if present
                    element = filterIfBeforePresent(element, before);
                    // clickText && equalsClick && multipleSelect
                    Optional<Element> elementOptional = findLink(element, clickText, equalsClick, multipleSelect);
                    return elementOptional;
                })
                // click link
                .map(doc -> HttpUtils.clickLink(doc, doc.ownText(), delayVariable))
                .map(Result::ok)
                .orElse(Result.error());
    }

    private Element filterIfAfterPresent(Element element, String text) {
        if (Objects.isNull(element) || StringUtils.isBlank(text)) {
            return element;
        }
        Elements links = element.select("a");
        // default skip all, if don't match any text.
        int skip = links.size();
        for (int i = 0; i < links.size(); i++) {
            Element link = links.get(i);
            String ownText = link.ownText();
            if (StringUtils.hasText(ownText)) {
                if (ownText.contains(text)) {
                    skip = i + 1;
                    break;
                }
            }
        }
        links = links.stream().skip(skip).collect(Collectors.toCollection(Elements::new));
        // keep base uri, don't change it.
        Element result = new Element("div");
        result.setBaseUri(element.baseUri());
        result.appendChildren(links);
        return result;
    }

    private Element filterIfBeforePresent(Element element, String text) {
        if (Objects.isNull(element) || StringUtils.isBlank(text)) {
            return element;
        }
        Elements links = element.select("a");
        // default limit 0, if don't match any text.
        int limit = 0;
        for (int i = 0; i < links.size(); i++) {
            Element link = links.get(i);
            String ownText = link.ownText();
            if (StringUtils.hasText(ownText)) {
                if (ownText.contains(text)) {
                    limit = i;
                    break;
                }
            }
        }
        links = links.stream().limit(limit).collect(Collectors.toCollection(Elements::new));
        // keep base uri, don't change it.
        Element result = new Element("div");
        result.setBaseUri(element.baseUri());
        result.appendChildren(links);
        return result;
    }

    private Optional<Element> findLink(Element element, String text, boolean equals, int select) {
        if (element == null || StringUtils.isBlank(text)) {
            return Optional.empty();
        }
        Optional<Element> elementOptional = Optional.empty();
        String[] keywords = text.split(";");
        for (String keyword : keywords) {
            elementOptional = equals ? ElementUtils.selectLinkByEquals(element, keyword, select)
                    : ElementUtils.selectLinkByLike(element, keyword, select);
            if (elementOptional.isPresent()) {
                return elementOptional;
            }
        }
        return elementOptional;
    }

    private boolean containsTextIfPresent(Element element, String text, boolean equals) {
        if (StringUtils.hasText(text)) {
            return findLink(element, text, equals, 1).isPresent();
        }
        return true;
    }

}
