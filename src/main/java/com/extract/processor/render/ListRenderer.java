package main.java.com.extract.processor.render;

import main.java.com.extract.processor.model.HtmlList;
import main.java.com.extract.processor.model.HtmlListElement;
import main.java.com.extract.processor.model.MText;


public class ListRenderer {
	// 1) no empty list
	// 2) must have a list item
	// 3) if header, leave as header not list item
	// 4) if first is header, place before list as list header/title
    public static String render(HtmlList list) {
        StringBuilder result = new StringBuilder();
        int liCnt = 0;
        // if no li then no list
        if (list.getTextList() != null) {
            for (MText text : list.getTextList()) {
                result.append(TextRenderer.render(text));
            }
        }
        if (list.getElementList() != null) {
            for (HtmlListElement listElement : list.getElementList()) {
            	if (listElement.isListItem()) {
            		liCnt++;
                    result.append(ListElementRenderer.render(listElement));
                } else if (listElement.isHeaderItem()) {
                    result.append(HeaderRender.render(listElement));
            	} else {
                    result.append(ParagraphRender.render(listElement));
            	}
            }
        }
        String txt = result.toString();
        if (txt.isEmpty()) return "";
        if (liCnt > 0) {
            String tagName = list.isSorted() ? "ol" : "ul";
        	return "<" + tagName + ">" + txt + "</" + tagName + ">";
        } else {
        	return txt;
        }
    }
}
