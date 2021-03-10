package org.sedro.yadoc.render;

import org.sedro.yadoc.model.MHeader;
import org.sedro.yadoc.model.MListElement;
import org.sedro.yadoc.model.MText;
import org.sedro.yadoc.utils.SimpleHtmlUtils;

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
