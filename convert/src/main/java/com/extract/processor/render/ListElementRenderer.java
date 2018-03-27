package com.extract.processor.render;

import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;
import com.extract.processor.render.ListRenderer;
import com.extract.processor.render.TextRenderer;

public class ListElementRenderer {
    public static String render(HtmlListElement element) {
        StringBuilder result = new StringBuilder();
        if (element.getTextList() != null) {
            for (Text text : element.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        if (element.getNestedList() != null) {
            result.append(ListRenderer.render(element.getNestedList()));
        }
        return "<li>" + result.toString() + "</li>";
    }
}
