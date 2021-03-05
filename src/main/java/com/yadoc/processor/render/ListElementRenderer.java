package main.java.com.yadoc.processor.render;

import main.java.com.yadoc.processor.model.MListElement;
import main.java.com.yadoc.processor.model.MText;
import main.java.com.yadoc.processor.utils.SimpleHtmlUtils;

public class ListElementRenderer {
    public static String render(MListElement element) {
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
    
    public static String renderText(MListElement element) {
        StringBuilder result = new StringBuilder();
        if (element.getTextList() != null) {
            for (MText text : element.getTextList()) {
                result.append(TextRenderer.renderText(text));
            }
        }
        if (element.getNestedList() != null) {
            result.append(ListRenderer.render(element.getNestedList()));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        if (txt.isEmpty()) return "";
        // add bullet if not there?
        return txt;
    }
}
