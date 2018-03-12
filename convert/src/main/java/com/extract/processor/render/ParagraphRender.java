package com.extract.processor.render;

import com.extract.processor.model.Paragraph;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;

public class ParagraphRender {

    @Getter
    @Setter
    private TextRenderer textRenderer;

    public String render(Paragraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (Text text : paragraph.getTexts()) {
            result.append(textRenderer.render(text));
        }
        return "<p>" + result.toString() + "</p>";
    }
}
