package com.extract.processor.utils;

import com.extract.processor.model.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
public class SimpleHtmlUtils {

    public static void clearSimpleHtml(SimpleHtml simpleHtml) {
        if (simpleHtml.getHeaderList() != null) {
            clearElements(simpleHtml.getHeaderList());
        }
        clearElements(simpleHtml.getElementList());
        if (simpleHtml.getFooterList() != null) {
            decreaseHeaders(simpleHtml.getFooterList());
            clearElements(simpleHtml.getFooterList());
        }
    }

    public static void decreaseHeaders(List<Element> elements) {
        for (Element element : elements) {
            if (element instanceof Header) {
                Header header = (Header) element;
                if (header.getLevel() < 6) {
                    header.setLevel(header.getLevel() + 1);
                }
            }
        }
    }

    public static void clearElements(List<Element> elements) {
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element instanceof Paragraph) {
                Paragraph paragraph = (Paragraph) element;
                clearText(paragraph.getTexts());
                if (paragraph.getTexts().isEmpty()) {
                    iterator.remove();
                }
            } else if (element instanceof HtmlList) {
                HtmlList htmlList = (HtmlList) element;
                if (clearList(htmlList)) {
                    iterator.remove();
                }
            } else if (element instanceof Header) {
                Header header = (Header) element;
                if (header.getText().trim().isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    public static void clearText(List<Text> texts) {
        Iterator<Text> iterator = texts.iterator();
        while (iterator.hasNext()) {
            Text text = iterator.next();
            if (text.getText().trim().isEmpty()) {
                iterator.remove();
            }
        }
    }

    public static boolean clearList(HtmlList htmlList) {
        boolean listIsEmpty = false;
        if (htmlList.getElementList() != null) {
            Iterator<HtmlListElement> iterator = htmlList.getElementList().iterator();
            while (iterator.hasNext()) {
                HtmlListElement htmlListElement = iterator.next();
                if (clearListItem(htmlListElement)) {
                    iterator.remove();
                }
            }
        }
        if (htmlList.getTextList() == null || htmlList.getTextList().isEmpty()) {
            if (htmlList.getElementList() == null || htmlList.getElementList().isEmpty()) {
                listIsEmpty = true;
            }
        }
        return listIsEmpty;
    }

    public static boolean clearListItem(HtmlListElement htmlListElement) {
        boolean listElementIsEmpty = false;
        clearText(htmlListElement.getTextList());
        if (htmlListElement.getTextList() == null || htmlListElement.getTextList().isEmpty()) {
            if (htmlListElement.getNestedList() == null) {
                listElementIsEmpty = true;
            }
        }
        if (!listElementIsEmpty) {
            if (htmlListElement.getNestedList() != null) {
                if (clearList(htmlListElement.getNestedList())) {
                    htmlListElement.setNestedList(null);
                }
            }
        }
        return listElementIsEmpty;
    }

    public static void optimizeSimpleHtml(SimpleHtml simpleHtml) {
        for (Element element : simpleHtml.getElementList()) {
            if (element instanceof Paragraph) {
                Paragraph paragraph = (Paragraph) element;
                paragraph.setTexts(optimizeTexts(paragraph.getTexts()));
            }
            if (element instanceof HtmlList) {
                HtmlList htmlList = (HtmlList) element;
                optimizeList(htmlList);
            }
        }
    }

    public static void optimizeList(HtmlList htmlList) {
        if (htmlList != null) {
            for (HtmlListElement htmlListElement : htmlList.getElementList()) {
                htmlListElement.setTextList(optimizeTexts(htmlListElement.getTextList()));
                optimizeList(htmlListElement.getNestedList());
            }
        }
    }

    public static List<Text> optimizeTexts(List<Text> texts) {
        if (texts != null) {
            List<Text> result = new ArrayList<>();
            if (texts.size() < 1) {
                return texts;
            }
            Text anchorText = texts.get(0);
            int anchorIndex = 0;
            for (int i = 0; i < texts.size(); i++) {
                if (i > 0) {
                    Text currentText = texts.get(i);
                    if (checkSimilarFontParams(anchorText, currentText)) {
                        continue;
                    }
                    result.add(joinTexts(texts.subList(anchorIndex, i)));
                    anchorText = currentText;
                    anchorIndex = i;
                }
            }
            result.add(joinTexts(texts.subList(anchorIndex, texts.size())));
            return result;
        }
        return null;
    }

    public static boolean checkSimilarFontParams(Text text1, Text text2) {
        return (text1.isItalic() == text2.isItalic())
                && (text1.isBold() == text2.isBold())
                && (text1.isUnderlined() == text2.isUnderlined());
    }

    public static Text joinTexts(List<Text> texts) {
        Text result = new Text();
        StringBuilder textContent = new StringBuilder();
        for (Text text : texts) {
            textContent.append(text.getText());
            result.setItalic(text.isItalic());
            result.setBold(text.isBold());
            result.setUnderlined(text.isUnderlined());
        }
        result.setText(textContent.toString());
        return result;
    }
}
