package org.sedro.yadoc.render;

import org.sedro.yadoc.model.MHeader;
import org.sedro.yadoc.model.MListElement;
import org.sedro.yadoc.model.MText;
import org.sedro.yadoc.utils.SimpleHtmlUtils;

public class HeaderRender {
    public static String render(MHeader header) {
        StringBuilder prefix = new StringBuilder();
        StringBuilder suffix = new StringBuilder();

        if (header.getLevel() > 0) {
        	prefix.append("<h").append(header.getLevel()).append(">");
        } else {
        	prefix.append("<p>");        	
        }
        if (header.isBold()) {
            prefix.append("<b>");
            suffix.insert(0, "</b>");
        }
        if (header.isItalic()) {
            prefix.append("<i>");
            suffix.insert(0, "</i>");
        }
        if (header.isUnderlined()) {
            prefix.append("<u>");
            suffix.insert(0, "</u>");
        }
        if (header.getLevel() > 0) {
        	suffix.append("</h").append(header.getLevel()).append(">");
        } else {
        	suffix.append("</p>");        	
        }
        prefix.append(header.getText());
        prefix.append(suffix.toString());
        return prefix.toString();
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
        if (header.getLevel() > 0) {
            return "<h" + hdr_level + ">" + txt + "</h" + hdr_level + ">";
        }
        return "<p>" + txt + "</p>";
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
