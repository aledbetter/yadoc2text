package com.extract.processor.render;

import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;

public class ListElementRenderer {

    @Getter
    @Setter
    private TextRenderer textRenderer;
    @Getter
    @Setter
    private ListRenderer listRenderer;

    public String render(HtmlListElement element) {
        StringBuilder result = new StringBuilder();
        if (element.getTextList() != null) {
            for (Text text : element.getTextList()) {
                result.append(textRenderer.render(text));
            }
        }
        if (element.getNestedList() != null) {
            result.append(listRenderer.render(element.getNestedList()));
        }
        return "<li>" + result.toString() + "</li>";
    }
}
