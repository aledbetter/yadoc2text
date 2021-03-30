package org.sedro.yadoc.model;


public class MText {
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private int fontSize;
    private int y; //coordinate

    private String text;
    
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
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isUpper() {
		int ccnt = 0;
	    for (int i=0; i<text.length(); i++){
	    	char c = text.charAt(i);
	        if (Character.isLowerCase(c)) return false;
	        if (Character.isLetter(c)) ccnt++;
	    }
	    if (ccnt < 1) return false;
	    return true;
	}	
	
}
