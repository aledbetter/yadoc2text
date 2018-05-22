package com.extract.processor.render;

import com.extract.processor.model.Header;
import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Text;
import com.extract.processor.render.TextRenderer;
import com.extract.processor.utils.SimpleHtmlUtils;

public class HeaderRender {
    public static String render(Header header) {
        return "<h" + header.getLevel() + ">" + header.getText() + "</h" + header.getLevel() + ">";
    }
    public static String render(HtmlListElement header) {
        StringBuilder result = new StringBuilder();
        if (header.getTextList() != null) {
            for (Text text : header.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        int hdr_level = header.getLevel();
        if (hdr_level < 1) hdr_level = 1;
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<h" + hdr_level + ">" + txt + "</h" + hdr_level + ">";
    }
}
