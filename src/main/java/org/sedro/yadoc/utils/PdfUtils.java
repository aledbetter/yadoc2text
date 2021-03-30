package org.sedro.yadoc.utils;

import org.apache.pdfbox.text.TextPosition;
import org.sedro.yadoc.model.MStyle;
import org.sedro.yadoc.model.MText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PdfUtils {

    public static final int HEADER_1_SIZE = 20;
    public static final int HEADER_2_SIZE = 18;
    public static final int HEADER_3_SIZE = 16;
    public static final int HEADER_4_SIZE = 14;
/*
 HDR_1[34][0][12]
 HDR_1[20][0][7]
 HDR_1[25][0][9]
 HDR_1[22][0][8]
 HDR_1[17][0][6]
 HDR_1[20][0][7]
 
 HDR_1[22][0][7] Cisco Inc
 HDR_1[19][0][6] Associate Recruiter
 HDR_1[16][0][5] Apr 2016 - Sept 2020
 HDR_1[17][0][6] Headed Engineering Development, QA and Operations/DevOps
 HDR_1[17][0][6] First employee, responsible for building technical teams to 50 and releasing 4 products
 HDR_1[17][0][6] Architected and documented cloud-based PBX, conference service, phone and messaging apps
 HDR_1[17][0][6] Responsible for budgeting, working with external partners, BOD presentations
 HDR_1[19][0][6] EDUCATION 
 
 */
    public static final int HEADER_1_HEIGHT = 14;
    public static final int HEADER_2_HEIGHT = 12;
    public static final int HEADER_3_HEIGHT = 10;
    public static final int HEADER_4_HEIGHT = 8;

    public static final List<String> listItemPattern;

    
    static {
        listItemPattern = new ArrayList<>();
        listItemPattern.add(".*\\x{2022}.*");
        listItemPattern.add(".*\\x{2023}.*");
        listItemPattern.add(".*\\x{25E6}.*");
        listItemPattern.add(".*\\x{2043}.*");
        listItemPattern.add(".*\\x{204C}.*");
        listItemPattern.add(".*\\x{204D}.*");
        listItemPattern.add(".*\\x{2219}.*");
        listItemPattern.add("^\\w\\. .*");
        listItemPattern.add("^\\+ .*");
        listItemPattern.add("^\\- .*");
        listItemPattern.add("^§ .*");
        //§
    }

    public static boolean isHeader(int fontSize) {
        return fontSize >= HEADER_4_SIZE;
    }

    public static boolean isTextHeightHeader(int fontSize) {
        return fontSize >= HEADER_4_HEIGHT;
    }
    

    public static boolean isHeader(HashMap<Integer, MStyle> hmap, TextPosition textPosition, String text) {
        int fontSize = (int) textPosition.getFontSize();
     //   int fontWeight = (int) textPosition.getFont().getFontDescriptor().getFontWeight();
        int textHeight = (int) textPosition.getHeight();
        boolean isBold = isBold(textPosition);
    		    	
        if (fontSize > 1) {
            return isHeader(fontSize);
        } else if (isBold) {
            return isBold;
        } else {
            return isTextHeightHeader(textHeight);
        }
    }

    public static int getLevelByFontSize(TextPosition lastHeader, int lastHeadingLevel, TextPosition textPosition) {
    	// if no last then 1
    	// if last is 1, then 1 OR 2
    	// if last is 2, then 1, 2, 3
    	// if same as last ??
    	
    	int fontSize = (int) textPosition.getFontSize();
        int fontWeight = (int) textPosition.getFont().getFontDescriptor().getFontWeight();
        int textHeight = (int) textPosition.getHeight();
        if (lastHeader != null) {
        	int lfontSize = (int) lastHeader.getFontSize();
            int lfontWeight = (int) lastHeader.getFont().getFontDescriptor().getFontWeight();
            int ltextHeight = (int) lastHeader.getHeight();   
            if (lfontSize == fontSize && lfontWeight == fontWeight && ltextHeight == textHeight) {
            	// same as last... same level
            	return lastHeadingLevel;
            }
        } 
        int level = 1;
        if (fontSize > 1) {
        	level = getLevelByFontSize(fontSize);
        } else if (fontWeight > 1) {
        	level = getLevelByFontSize(fontWeight);
        } else {
        	level = getLevelByTextHeight(textHeight);
        }
        if (lastHeader != null) {
        	if (level > (lastHeadingLevel+1)) {
        		level = lastHeadingLevel+1; // go one deeper only
        	}
        } else if (level > 2) {
        	// first heading is 1 or 2...
        	level = 2;
        }
        return level;
    }

    public static int getLevelByTextHeight(int textHeight) {
        if (textHeight >= HEADER_1_HEIGHT) {
            return 1;
        } else if (textHeight >= HEADER_2_HEIGHT) {
            return 2;
        } else if (textHeight >= HEADER_3_HEIGHT) {
            return 3;
        } else {
            return 4;
        }
    }

    public static int getLevelByFontSize(int fontSize) {
        if (fontSize >= HEADER_1_SIZE) {
            return 1;
        } else if (fontSize >= HEADER_2_SIZE) {
            return 2;
        } else if (fontSize >= HEADER_3_SIZE) {
            return 3;
        } else {
            return 4;
        }
    }

    public static boolean isBold(TextPosition textPosition) {
        if (textPosition.getFont().getFontDescriptor().isForceBold()) {
            return true;
        }

        int fontWeight = (int) textPosition.getFont().getFontDescriptor().getFontWeight();
        if (fontWeight > 680) {
            return true;
        }

        String fontName = textPosition.getFont().getName();
        if (fontName.toLowerCase().contains("bold")) {
            return true;
        }
        return false;
    }

    public static boolean isItalic(TextPosition textPosition) {
        if (textPosition.getFont().getFontDescriptor().isItalic()) {
            return true;
        }

        int italicAngle = (int) textPosition.getFont().getFontDescriptor().getItalicAngle();

        if (italicAngle != 0) {
            return true;
        }

        String fontName = textPosition.getFont().getName();

        if (fontName.toLowerCase().contains("italic")) {
            return true;
        }
        return false;
    }

    public static boolean isListItem(String text) {
        for (String listItemPrefix : listItemPattern) {
            if (text.trim().matches(listItemPrefix)) {
                return true;
            }
        }
        return false;
    }

    public static float getWordWidth(List<TextPosition> textPositions) {
    	float ww = 0;
        for (TextPosition textPosition : textPositions) {
        	char c = textPosition.getUnicode().charAt(0);
        	if (Character.isWhitespace(c)) break;
        	ww += textPosition.getWidth();
        }
        return ww;
    }
    public static List<MText> convertText(List<TextPosition> textPositions) {
        List<MText> result = new ArrayList<>();
        String txt = "";
        int len = 0;
        boolean cb = false;
        boolean ci = false;
        int y = 0;
        // link together in chains... if format update then break 
        for (TextPosition textPosition : textPositions) {
        	boolean it = PdfUtils.isItalic(textPosition);
        	boolean b = PdfUtils.isBold(textPosition);
        	if (it != ci || b != cb) {
        		if (len > 0) { // add due to change
	                MText simpleText = new MText();
	                simpleText.setText(txt);
	                txt = txt.replace("\t", " ").replace("§ ", "");
	                simpleText.setItalic(ci);
	                simpleText.setBold(cb);
	                result.add(simpleText);  
        		}
        		cb = b;
        		ci = it;
        		len = 1;
        		txt = textPosition.getUnicode();
        	} else {
        		len++;
        		txt += textPosition.getUnicode();
        	}
        	y = (int)textPosition.getEndY();
        }
        
    	if (len > 0) {
            MText simpleText = new MText();
            txt = txt.replace("\t", " ").replace("§ ", "");
            simpleText.setText(txt);
            simpleText.setItalic(ci);
            simpleText.setBold(cb);      
            simpleText.setY(y);
            result.add(simpleText);
    	}
        return result;
    }

}
