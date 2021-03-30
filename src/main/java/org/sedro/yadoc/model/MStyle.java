package org.sedro.yadoc.model;

public class MStyle {
	private int level = 0;
	private int fontSize = 0;
	private int count = 0;
	private int count_char = 0;
	private boolean bold;
	private boolean italic;
	private boolean underlined;
	private boolean upper;		// all uppercase
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount_char() {
		return count_char;
	}
	public void setCount_char(int count_char) {
		this.count_char = count_char;
	}
	public boolean isBold() {
		return bold;
	}
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	public boolean isItalic() {
		return italic;
	}
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
	public boolean isUnderlined() {
		return underlined;
	}
	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}
	public boolean isUpper() {
		return upper;
	}
	public void setUpper(boolean upper) {
		this.upper = upper;
	}

}
