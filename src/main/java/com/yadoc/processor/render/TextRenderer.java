package main.java.com.yadoc.processor.render;

import main.java.com.yadoc.processor.model.MText;
import main.java.com.yadoc.processor.utils.SimpleHtmlUtils;

public class TextRenderer {
    public static String render(MText text) {

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
        String txt = SimpleHtmlUtils.cleanTexts(text.getText());
        if (txt.trim().length() > 0) { // fmt only if something to fmt
        	txt = prefix.toString() + txt + suffix.toString();
        }
        
        return txt;
    }
    public static String renderText(MText text) {

        StringBuilder prefix = new StringBuilder();
        StringBuilder suffix = new StringBuilder();

        String txt = SimpleHtmlUtils.cleanTexts(text.getText());
        if (txt.trim().length() > 0) { // fmt only if something to fmt
        	txt = prefix.toString() + txt + suffix.toString();
        }
        
        return txt;
    }
}
