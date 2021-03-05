package main.java.com.yadoc.processor.model;


import java.util.List;


public class MList implements MElement {
    private List<MText> textList;
    private List<MListElement> elementList;
    private boolean sorted;
    
	public List<MText> getTextList() {
		return textList;
	}
	public void setTextList(List<MText> textList) {
		this.textList = textList;
	}
	public List<MListElement> getElementList() {
		return elementList;
	}
	public void setElementList(List<MListElement> elementList) {
		this.elementList = elementList;
	}
	public boolean isSorted() {
		return sorted;
	}
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
}
