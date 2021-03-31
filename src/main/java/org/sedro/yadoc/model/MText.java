package org.sedro.yadoc.model;


public class MText {
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private int fontSize;
    private int x; //coordinate
    private int startx; //coordinate
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
	public int getStartY() {
		return getY()-getFontSize();
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getStartX() {
		return startx;
	}
	public void setStartX(int x) {
		this.startx = x;
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
	
	// check if overlap X coordinate
	public int isOverlapX(MText other) {
		if (this.getY() <= 0) return 0;
		if (other.getX() <= this.getX() && other.getX() >= this.getStartX()) {
			return other.getX()-this.getStartX();
		}
		if (other.getStartX() >= this.getStartX() && other.getStartX() <= this.getX()) {
			return this.getX()-other.getStartX();
		}
	    return 0;
	}	
	// check if overlap X coordinate
	public int isOverlapY(MText other) {
		if (this.getY() <= 0) return 0;
		if (other.getY() <= this.getY() && other.getY() >= this.getStartY()) {
			return other.getY()-this.getStartY();
		}
		if (other.getStartY() >= this.getStartY() && other.getStartY() <= this.getY()) {
			return this.getY()-other.getStartY();
		}		
	    return 0;
	}	
	
}
