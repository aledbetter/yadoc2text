package com.extract.processor.render;

import com.extract.processor.model.Paragraph;
import com.extract.processor.model.Text;
import lombok.Getter;
import lombok.Setter;
import main.java.com.convert.processor.render.TextRenderer;

public class ParagraphRender {
    public static String render(Paragraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (Text text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = result.toString();
        txt = txt.replace("\t", "    "); // replace tabs always
        txt = txt.replace("\\u0000", ""); // replace null chars (geting in doc/pdf from time to time)
        return "<p>" + txt + "</p>";
    }
}
