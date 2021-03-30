package org.sedro.yadoc.model;


public class MHeader extends MText implements MElement {
    private int level;

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
