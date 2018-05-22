package com.extract.processor.render;

import com.extract.processor.model.Paragraph;
import com.extract.processor.model.Text;
import com.extract.processor.model.HtmlListElement;
import com.extract.processor.render.TextRenderer;
import com.extract.processor.utils.SimpleHtmlUtils;
import lombok.Getter;
import lombok.Setter;

public class ParagraphRender {
    public static String render(Paragraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (Text text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<p>" + txt + "</p>";
    }
    public static String render(HtmlListElement paragraph) {
        StringBuilder result = new StringBuilder();
        if (paragraph.getTextList() != null) {
            for (Text text : paragraph.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        //System.out.println("P1["+result.toString()+"]");
        //System.out.println("P2["+txt+"]");

        return "<p>" + txt + "</p>";
    }
}
