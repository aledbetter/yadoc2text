package com.extract.processor.model;

import lombok.Data;
import com.extract.processor.model.HtmlList;
import com.extract.processor.model.Text;

import java.util.List;

@Data
public class HtmlListElement {
    private List<Text> textList;
    private HtmlList nestedList;
    private String tagName;
    private int level;

    public boolean isListItem() {
    	if (tagName == null || tagName.equals("li")) return true;
    	return false;
    }
}
