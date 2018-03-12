package com.extract.processor.render;

import com.extract.processor.model.HtmlList;
import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;

public class ListRenderer {

    @Getter
    @Setter
    private TextRenderer textRenderer;
    @Getter
    @Setter
    private ListElementRenderer listElementRenderer;

    public String render(HtmlList list) {
        String tagName = list.isSorted() ? "ol" : "ul";
        StringBuilder result = new StringBuilder();
        if (list.getTextList() != null) {
            for (Text text : list.getTextList()) {
                result.append(textRenderer.render(text));
            }
        }
        if (list.getElementList() != null) {
            for (HtmlListElement listElement : list.getElementList()) {
                result.append(listElementRenderer.render(listElement));
            }
        }
        return "<" + tagName + ">" + result.toString() + "</" + tagName + ">";
    }
}
