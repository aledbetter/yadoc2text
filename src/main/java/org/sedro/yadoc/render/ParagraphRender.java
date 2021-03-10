package org.sedro.yadoc.render;

import org.sedro.yadoc.model.MListElement;
import org.sedro.yadoc.model.MParagraph;
import org.sedro.yadoc.model.MText;
import org.sedro.yadoc.utils.SimpleHtmlUtils;


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
