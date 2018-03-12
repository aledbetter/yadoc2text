package com.extract.processor.render;

import com.extract.processor.model.Text;

public class TextRenderer {
    public String render(Text text) {

        StringBuilder prefix = new StringBuilder();
        StringBuilder suffix = new StringBuilder();

        if (text.isBold()) {
            prefix.append("<b>");
            suffix.insert(0, "</b>");
        }
        if (text.isItalic()) {
            prefix.append("<i>");
            suffix.insert(0, "</i>");
        }
        if (text.isUnderlined()) {
            prefix.append("<u>");
            suffix.insert(0, "</u>");
        }
        return prefix.toString() + text.getText() + suffix.toString();
    }
}
