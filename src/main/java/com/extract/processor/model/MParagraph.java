package main.java.com.extract.processor.model;


import java.util.List;

public class MParagraph implements MElement {
    private List<MText> texts;

	public List<MText> getTexts() {
		return texts;
	}

	public void setTexts(List<MText> texts) {
		this.texts = texts;
	}
}
