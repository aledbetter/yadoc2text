package main.java.com.extract.processor.render;

import main.java.com.extract.processor.model.MElement;
import main.java.com.extract.processor.model.MHeader;
import main.java.com.extract.processor.model.HtmlList;
import main.java.com.extract.processor.model.MParagraph;
import main.java.com.extract.processor.model.SimpleHtml;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;

import java.util.List;

public class SimpleHtmlRender {
    public static StringBuilder renderHdr(SimpleHtml simpleHtml) {
        StringBuilder result = new StringBuilder();
        result.append("<html><head>");
        if (simpleHtml.getTitle() != null) {
            result
                    .append("<title>")
                    .append(SimpleHtmlUtils.cleanTexts(simpleHtml.getTitle()))
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
        return result;
    }
    public static StringBuilder renderFtr(SimpleHtml simpleHtml, StringBuilder result) {
        if (simpleHtml.getFooterList() != null && simpleHtml.getFooterList().size() > 0) {
            result.append("<footer>");
            renderElementList(simpleHtml.getFooterList(), result);
            result.append("</footer>");
        }
        result.append("</body></html>");
        return result;
    }
    
    public static String render(SimpleHtml simpleHtml) {
        StringBuilder result = renderHdr(simpleHtml);
        renderElementList(simpleHtml.getElementList(), result);
        result = renderFtr(simpleHtml, result);
        return result.toString();
    }

    private static void renderElementList(List<MElement> elementList, StringBuilder result) {
        for (MElement element : elementList) {
            if (element instanceof MHeader) {
                result.append(HeaderRender.render((MHeader) element));
            } else if (element instanceof MParagraph) {
                result.append(ParagraphRender.render((MParagraph) element));
            } else if (element instanceof HtmlList) {
                result.append(ListRenderer.render((HtmlList) element));
            }
        }
    }
    private static void renderElementListText(List<MElement> elementList, StringBuilder result) {
        for (MElement element : elementList) {
            if (element instanceof MHeader) {
                result.append(HeaderRender.renderText((MHeader) element));
            } else if (element instanceof MParagraph) {
                result.append(ParagraphRender.renderText((MParagraph) element));
            } else if (element instanceof HtmlList) {
                result.append(ListRenderer.renderText((HtmlList) element));
            }
        }
    }
    
    public static String renderText(SimpleHtml simpleHtml) {
        StringBuilder result = new StringBuilder();
        if (simpleHtml.getTitle() != null) {
            result.append(SimpleHtmlUtils.cleanTexts(simpleHtml.getTitle()));
            result.append('\n');
        }
        if (simpleHtml.getHeaderList() != null && simpleHtml.getHeaderList().size() > 0) {
            renderElementListText(simpleHtml.getHeaderList(), result);
            result.append('\n');
        }
        
        // content
        renderElementListText(simpleHtml.getElementList(), result);
        
        
        if (simpleHtml.getFooterList() != null && simpleHtml.getFooterList().size() > 0) {
            result.append('\n');
            renderElementListText(simpleHtml.getFooterList(), result);
        }
        result.append('\n');
        return result.toString();    	
    }

}
