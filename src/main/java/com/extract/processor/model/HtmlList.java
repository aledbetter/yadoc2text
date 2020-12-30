package main.java.com.extract.processor.model;


import java.util.List;


public class HtmlList implements MElement {
    private List<MText> textList;
    private List<HtmlListElement> elementList;
    private boolean sorted;
    
	public List<MText> getTextList() {
		return textList;
	}
	public void setTextList(List<MText> textList) {
		this.textList = textList;
	}
	public List<HtmlListElement> getElementList() {
		return elementList;
	}
	public void setElementList(List<HtmlListElement> elementList) {
		this.elementList = elementList;
	}
	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
}
