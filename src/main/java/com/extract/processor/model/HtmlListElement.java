package main.java.com.extract.processor.model;


import java.util.List;

public class HtmlListElement {
    private List<MText> textList;
    private HtmlList nestedList;
    private String tagName;
    private int level;

    
	public List<MText> getTextList() {
		return textList;
	}
	public void setTextList(List<MText> textList) {
		this.textList = textList;
	}
	public HtmlList getNestedList() {
		return nestedList;
	}
	public void setNestedList(HtmlList nestedList) {
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
