package com.extract.processor.utils;

import com.extract.processor.model.*;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class HtmlUtils {

    public static final Set<String> textTags;
    public static final Set<String> styleTags;
    public static final Set<String> listTags;
    public static final Set<String> headerTags;
    public static final Set<String> ignoredTags;
    public static final Set<String> containerTags;

    private static final Pattern headerPattern = Pattern.compile("^h([123456])$");

    static {
        textTags = new HashSet<>();
        textTags.add("p");
        textTags.add("a");
        textTags.add("span");
        styleTags = new HashSet<>();
        styleTags.add("b");
        styleTags.add("i");
        styleTags.add("u");
        styleTags.add("strong");
        styleTags.add("em");
        listTags = new HashSet<>();
        listTags.add("ul");
        listTags.add("ol");
        headerTags = new HashSet<>();
        headerTags.add("h1");
        headerTags.add("h2");
        headerTags.add("h3");
        headerTags.add("h4");
        headerTags.add("h5");
        headerTags.add("h6");
        ignoredTags = new HashSet<>();
        ignoredTags.add("script");
        ignoredTags.add("style");
        ignoredTags.add("meta");
        ignoredTags.add("link");
        ignoredTags.add("img");
        ignoredTags.add("noscript");
        ignoredTags.add("svg");
        ignoredTags.add("button");
        ignoredTags.add("input");
        ignoredTags.add("nav");
        ignoredTags.add("annotation");
        containerTags = new HashSet<>();
        containerTags.add("div");
        containerTags.add("nav");
        containerTags.add("section");
        containerTags.add("article");
        containerTags.add("iframe");
    }

    public static void simplify(Element element, SimpleHtml simpleHtml) {
        for (Element child : element.children()) {
            String tagName = child.tagName();
            //String roleName = child.attr("role");
            //String idName = child.attr("id");

            if (!ignoredTags.contains(tagName)) {
                if (headerTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processHeader(child));
                    } else {
                        simpleHtml.getFooterList().add(processHeader(child));
                    }
                } else if (tagName.equals("title")) {
                    simpleHtml.setTitle(child.text());
                } else if (tagName.equals("header")) {// || tagName.equals("nav")) {
                    simplify(child, simpleHtml.getHeaderList());
                } else if (tagName.equals("footer")) {
                    simplify(child, simpleHtml.getFooterList());
                } else if (listTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processList(child));
                    } else {
                        simpleHtml.getFooterList().add(processList(child));
                    }
                } else if (textTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processParagraph(child));
                    } else {
                        simpleHtml.getFooterList().add(processParagraph(child));
                    }
                } else {
                    simplify(child, simpleHtml);
                }
            }
        }
    }

    public static void simplify(Element element,
                                List<com.extract.processor.model.Element> elements) {
        for (Element child : element.children()) {
            String tagName = child.tagName();
            if (!ignoredTags.contains(tagName)) {
                if (headerTags.contains(tagName)) {
                    elements.add(processHeader(child));
                } else if (listTags.contains(tagName)) {
                    elements.add(processList(child));
                } else if (textTags.contains(tagName)) {
                    elements.add(processParagraph(child));
                } else {
                    simplify(child, elements);
                }
            }
        }
    }

    public static Paragraph processParagraph(Element element) {
        Paragraph paragraph = new Paragraph();
        paragraph.setTexts(processText(element));
        return paragraph;
    }

    public static Header processHeader(Element element) {
        Header header = new Header();
        header.setLevel(getHeaderLevel(element.tagName()));
        header.setText(element.text());
        return header;
    }

    public static int getHeaderLevel(String headerTagName) {
        Matcher matcher = headerPattern.matcher(headerTagName);
        if (!matcher.matches()) {
            throw new RuntimeException("invalid header tag name: " + headerTagName);
        }
        return Integer.valueOf(matcher.group(1));
    }

    public static HtmlList processList(Element element) {

        HtmlList htmlList = new HtmlList();
        htmlList.setTextList(new ArrayList<Text>());
        htmlList.setElementList(new ArrayList<HtmlListElement>());

        for (Element child : element.children()) {
            String tagName = child.tagName();
            if (tagName.equals("li")) {
                htmlList.getElementList().add(processListElement(child));
            } else {
                htmlList.getTextList().addAll(processText(child));
            }
        }

        return htmlList;
    }

    public static HtmlListElement processListElement(Element element) {

        HtmlListElement result = new HtmlListElement();
        result.setTextList(new ArrayList<Text>());

        if (containsOnlyFormattedText(element)) {
            result.setTextList(processText(element));
            return result;
        }

        for (Element child : element.children()) {
            String tagName = child.tagName();
            if (listTags.contains(tagName)) {
                result.setNestedList(processList(child));
            } else {
                result.getTextList().addAll(processText(child));
            }
        }

        return result;
    }

    public static List<Text> processText(Element element) {
        List<Text> result = new ArrayList<>();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                Text text = new Text();
                text.setText(((TextNode) node).text());
                text.setBold(isBold(node));
                text.setItalic(isItalic(node));
                text.setUnderlined(isUnderlined(node));
                result.add(text);
            } else if (node instanceof Element) {
                result.addAll(processText((Element) node));
            }
        }
        return result;
    }

    public static boolean containsOnlyFormattedText(Element element) {
        if (element.hasText()) {
            for (Element child : element.getAllElements()) {
                if (element != child) {
                    String tagName = child.tagName();
                    if (!styleTags.contains(tagName)) {
                        if (!containerTags.contains(tagName)) {
                            if (!textTags.contains(tagName)) {
                                if (!headerTags.contains(tagName)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBold(Node node) {
        return (isStyled(node, "b") || isStyled(node, "strong"));
    }

    public static boolean isItalic(Node node) {
        return (isStyled(node, "i") || isStyled(node, "em"));
    }

    public static boolean isUnderlined(Node node) {
        return isStyled(node, "u");
    }

    public static boolean isStyled(Node node, String styleTagName) {
        Element container = null;
        if (node instanceof TextNode) {
            container = (Element) node.parent();
        } else if (node instanceof Element) {
            container = (Element) node;
        }
        if (container != null) {
            if (container.tagName().equals(styleTagName)) {
                return true;
            }
            for (Element parent : container.parents()) {
                if (parent.tagName().equals(styleTagName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
