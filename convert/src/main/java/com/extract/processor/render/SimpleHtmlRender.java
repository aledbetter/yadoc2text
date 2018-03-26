package com.extract.processor.render;

import com.extract.processor.model.*;
import lombok.Getter;
import lombok.Setter;
import main.java.com.convert.processor.model.Element;
import main.java.com.convert.processor.model.Header;
import main.java.com.convert.processor.model.HtmlList;
import main.java.com.convert.processor.model.Paragraph;
import main.java.com.convert.processor.model.SimpleHtml;
import main.java.com.convert.processor.render.HeaderRender;
import main.java.com.convert.processor.render.ListRenderer;
import main.java.com.convert.processor.render.ParagraphRender;

import java.util.List;

public class SimpleHtmlRender {

    public static String render(SimpleHtml simpleHtml) {
        StringBuilder result = new StringBuilder();
        result.append("<html><head>");
        if (simpleHtml.getTitle() != null) {
            result
                    .append("<title>")
                    .append(simpleHtml.getTitle())
                    .append("</title>");
        }
        if (simpleHtml.getType() != null) {
            result
                    .append("<meta name=\"doc-type\" content=\"")
                    .append(simpleHtml.getType()).append("\"/>");
        }
        if (simpleHtml.getName() != null) {
            result
                    .append("<meta name=\"doc-name\" content=\"")
                    .append(simpleHtml.getName()).append("\"/>");
        }
        if (simpleHtml.getCreated() != null) {
            result
                    .append("<meta name=\"doc-created\" content=\"")
                    .append(simpleHtml.getCreated()).append("\"/>");
        }
        if (simpleHtml.getModified() != null) {
            result
                    .append("<meta name=\"doc-modified\" content=\"")
                    .append(simpleHtml.getModified()).append("\"/>");
        }
        if (simpleHtml.getLanguage() != null) {
            result
                    .append("<meta name=\"doc-language\" content=\"")
                    .append(simpleHtml.getLanguage()).append("\"/>");
        }
        if (simpleHtml.getUrl() != null) {
            result
                    .append("<meta name=\"doc-url\" content=\"")
                    .append(simpleHtml.getUrl()).append("\"/>");
        }
        result.append("</head><body>");
        if (simpleHtml.getHeaderList() != null && simpleHtml.getHeaderList().size() > 0) {
            result.append("<header>");
            renderElementList(simpleHtml.getHeaderList(), result);
            result.append("</header>");
        }
        renderElementList(simpleHtml.getElementList(), result);
        if (simpleHtml.getFooterList() != null && simpleHtml.getFooterList().size() > 0) {
            result.append("<footer>");
            renderElementList(simpleHtml.getFooterList(), result);
            result.append("</footer>");
        }
        result.append("</body></html>");
        return result.toString();
    }

    private static void renderElementList(List<Element> elementList, StringBuilder result) {
        for (Element element : elementList) {
            if (element instanceof Header) {
                result.append(HeaderRender.render((Header) element));
            } else if (element instanceof Paragraph) {
                result.append(ParagraphRender.render((Paragraph) element));
            } else if (element instanceof HtmlList) {
                result.append(ListRenderer.render((HtmlList) element));
            }
        }
    }
}
