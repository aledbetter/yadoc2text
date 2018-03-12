package com.extract.processor.utils;

import com.extract.processor.model.Text;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.text.TextPosition;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class PdfUtils {

    public static final int HEADER_1_SIZE = 20;
    public static final int HEADER_2_SIZE = 18;
    public static final int HEADER_3_SIZE = 16;
    public static final int HEADER_4_SIZE = 14;

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
    }

    public static boolean isHeader(int fontSize) {
        return fontSize >= HEADER_4_SIZE;
    }

    public static boolean isTextHeightHeader(int fontSize) {
        return fontSize >= HEADER_4_HEIGHT;
    }

    public static boolean isHeader(TextPosition textPosition) {
        int fontSize = (int) textPosition.getFontSize();
        int fontWeight = (int) textPosition.getFont().getFontDescriptor().getFontWeight();
        int textHeight = (int) textPosition.getHeight();
        if (fontSize > 1) {
            return isHeader(fontSize);
        } else if (fontWeight > 1) {
            return isHeader(fontWeight);
        } else {
            return isTextHeightHeader(textHeight);
        }
    }

    public static int getLevelByFontSize(TextPosition textPosition) {
        int fontSize = (int) textPosition.getFontSize();
        int fontWeight = (int) textPosition.getFont().getFontDescriptor().getFontWeight();
        int textHeight = (int) textPosition.getHeight();
        if (fontSize > 1) {
            return getLevelByFontSize(fontSize);
        } else if (fontWeight > 1) {
            return getLevelByFontSize(fontWeight);
        } else {
            return getLevelByTextHeight(textHeight);
        }
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

    public static List<Text> convertText(List<TextPosition> textPositions) {
        List<Text> result = new ArrayList<>();
        for (TextPosition textPosition : textPositions) {
            Text simpleText = new Text();
            simpleText.setText(textPosition.getUnicode());
            simpleText.setItalic(PdfUtils.isItalic(textPosition));
            simpleText.setBold(PdfUtils.isBold(textPosition));
            result.add(simpleText);
        }
        return result;
    }

}
