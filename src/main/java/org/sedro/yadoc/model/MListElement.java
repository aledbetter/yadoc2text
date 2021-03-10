package org.sedro.yadoc.model;


import java.util.List;

public class MListElement {
    private List<MText> textList;
    private MList nestedList;
    private String tagName;
    private int level;

    
	public List<MText> getTextList() {
		return textList;
	}
	public void setTextList(List<MText> textList) {
		this.textList = textList;
	}
	public MList getNestedList() {
		return nestedList;
	}
	public void setNestedList(MList nestedList) {
		this.nestedList = nestedList;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
    public boolean isListItem() {
    	if (tagName == null || tagName.equals("li")) return true;
    	return false;
    }
    public boolean isHeaderItem() {
    	if (tagName == null || tagName.startsWith("h")) return true;
    	return false;
    }
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
