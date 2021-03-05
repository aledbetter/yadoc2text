package main.java.com.yadoc.processor.model;


public class MText {
    private boolean bold;
    private boolean italic;
    private boolean underlined;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
