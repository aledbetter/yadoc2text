package com.extract.processor.render;

import com.extract.processor.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SimpleHtmlRender {

    public static SimpleHtmlRender factoryMethod() {
        SimpleHtmlRender simpleHtmlRender = new SimpleHtmlRender();
        simpleHtmlRender.setHeaderRender(new HeaderRender());

        ParagraphRender paragraphRender = new ParagraphRender();
        paragraphRender.setTextRenderer(new TextRenderer());

        simpleHtmlRender.setParagraphRender(paragraphRender);

        ListRenderer listRenderer = new ListRenderer();

        ListElementRenderer listElementRenderer = new ListElementRenderer();
        listElementRenderer.setListRenderer(listRenderer);
        listElementRenderer.setTextRenderer(new TextRenderer());

        listRenderer.setListElementRenderer(listElementRenderer);
        listRenderer.setTextRenderer(new TextRenderer());

        simpleHtmlRender.setListRenderer(listRenderer);

        return simpleHtmlRender;
    }

    @Getter
    @Setter
    private ParagraphRender paragraphRender;
    @Getter
    @Setter
    private HeaderRender headerRender;
    @Getter
    @Setter
    private ListRenderer listRenderer;

    public String render(SimpleHtml simpleHtml) {
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
                    .append(simpleHtml.getType()).append("\">");
        }
        if (simpleHtml.getName() != null) {
            result
                    .append("<meta name=\"doc-name\" content=\"")
                    .append(simpleHtml.getName()).append("\">");
        }
        if (simpleHtml.getCreated() != null) {
            result
                    .append("<meta name=\"doc-created\" content=\"")
                    .append(simpleHtml.getCreated()).append("\">");
        }
        if (simpleHtml.getModified() != null) {
            result
                    .append("<meta name=\"doc-modified\" content=\"")
                    .append(simpleHtml.getModified()).append("\">");
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

    private void renderElementList(List<Element> elementList, StringBuilder result) {
        for (Element element : elementList) {
            if (element instanceof Header) {
                result.append(headerRender.render((Header) element));
            } else if (element instanceof Paragraph) {
                result.append(paragraphRender.render((Paragraph) element));
            } else if (element instanceof HtmlList) {
                result.append(listRenderer.render((HtmlList) element));
            }
        }
    }
}
