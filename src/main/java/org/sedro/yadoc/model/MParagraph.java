package org.sedro.yadoc.model;


import java.util.List;

public class MParagraph implements MElement {
    private List<MText> texts;

	public List<MText> getTexts() {
		return texts;
	}

	public void setTexts(List<MText> texts) {
		this.texts = texts;
	}
	public void addTexts(List<MText> texts) {
		this.texts.addAll(texts);
	}
}
