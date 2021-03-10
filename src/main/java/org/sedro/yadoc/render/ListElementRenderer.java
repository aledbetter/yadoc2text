package org.sedro.yadoc.render;

import org.sedro.yadoc.model.MListElement;
import org.sedro.yadoc.model.MText;
import org.sedro.yadoc.utils.SimpleHtmlUtils;

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
