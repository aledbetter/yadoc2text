package main.java.com.extract.processor.render;

import main.java.com.extract.processor.model.MParagraph;
import main.java.com.extract.processor.model.MText;
import main.java.com.extract.processor.model.HtmlListElement;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;


public class ParagraphRender {
    public static String render(MParagraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (MText text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<p>" + txt + "</p>";
    }
    public static String render(HtmlListElement paragraph) {
        StringBuilder result = new StringBuilder();
        if (paragraph.getTextList() != null) {
            for (MText text : paragraph.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        //System.out.println("P1["+result.toString()+"]");
        //System.out.println("P2["+txt+"]");

        return "<p>" + txt + "</p>";
    }
    
    public static String renderText(MParagraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (MText text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return txt + "\n";
    }
    public static String renderText(HtmlListElement paragraph) {
        StringBuilder result = new StringBuilder();
        if (paragraph.getTextList() != null) {
            for (MText text : paragraph.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        //System.out.println("P1["+result.toString()+"]");
        //System.out.println("P2["+txt+"]");

        return txt + "\n";
    }
}
