package com.extract.processor.render;

import com.extract.processor.model.Paragraph;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;
import com.extract.processor.render.TextRenderer;
import com.extract.processor.utils.SimpleHtmlUtils;

public class ParagraphRender {
    public static String render(Paragraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (Text text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<p>" + txt + "</p>";
    }
}
