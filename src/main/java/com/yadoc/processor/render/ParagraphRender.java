package main.java.com.yadoc.processor.render;

import main.java.com.yadoc.processor.model.MListElement;
import main.java.com.yadoc.processor.model.MParagraph;
import main.java.com.yadoc.processor.model.MText;
import main.java.com.yadoc.processor.utils.SimpleHtmlUtils;


public class ParagraphRender {
    public static String render(MParagraph paragraph) {
        StringBuilder result = new StringBuilder();
        for (MText text : paragraph.getTexts()) {
            result.append(TextRenderer.render(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return "<p>" + txt + "</p>";
    }
    public static String render(MListElement paragraph) {
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
            result.append(TextRenderer.renderText(text));
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        return txt + "\n";
    }
    public static String renderText(MListElement paragraph) {
        StringBuilder result = new StringBuilder();
        if (paragraph.getTextList() != null) {
            for (MText text : paragraph.getTextList()) {
                result.append(TextRenderer.renderText(text));
            }
        }
        String txt = SimpleHtmlUtils.cleanTexts(result.toString());
        //System.out.println("P1["+result.toString()+"]");
        //System.out.println("P2["+txt+"]");

        return txt + "\n";
    }
}
