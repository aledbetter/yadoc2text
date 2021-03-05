package main.java.com.extract.processor.render;

import main.java.com.extract.processor.model.MHeader;
import main.java.com.extract.processor.model.MListElement;
import main.java.com.extract.processor.model.MText;
import main.java.com.extract.processor.render.TextRenderer;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;

public class HeaderRender {
    public static String render(MHeader header) {
        return "<h" + header.getLevel() + ">" + header.getText() + "</h" + header.getLevel() + ">";
    }
    public static String render(MListElement header) {
        StringBuilder result = new StringBuilder();
        if (header.getTextList() != null) {
            for (MText text : header.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        int hdr_level = header.getLevel();
        if (hdr_level < 1) hdr_level = 1;
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<h" + hdr_level + ">" + txt + "</h" + hdr_level + ">";
    }
    
    public static String renderText(MHeader header) {
        return header.getText() + "\n";
    }
    public static String renderText(MListElement header) {
        StringBuilder result = new StringBuilder();
        if (header.getTextList() != null) {
            for (MText text : header.getTextList()) {
                result.append(TextRenderer.renderText(text));
            }
        }
        int hdr_level = header.getLevel();
        if (hdr_level < 1) hdr_level = 1;
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return txt + "\n";
    }
}
