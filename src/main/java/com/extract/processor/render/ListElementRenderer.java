package main.java.com.extract.processor.render;

import main.java.com.extract.processor.model.HtmlListElement;
import main.java.com.extract.processor.model.MText;

import main.java.com.extract.processor.utils.SimpleHtmlUtils;

public class ListElementRenderer {
    public static String render(HtmlListElement element) {
        StringBuilder result = new StringBuilder();
        if (element.getTextList() != null) {
            for (MText text : element.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        if (element.getNestedList() != null) {
            result.append(ListRenderer.render(element.getNestedList()));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        if (txt.isEmpty()) return "";
        return "<li>" + txt+ "</li>";
    }
}
