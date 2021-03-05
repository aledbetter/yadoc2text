package main.java.com.extract.processor.utils;

import main.java.com.extract.processor.model.MElement;
import main.java.com.extract.processor.model.MHeader;
import main.java.com.extract.processor.model.MList;
import main.java.com.extract.processor.model.MListElement;
import main.java.com.extract.processor.model.MParagraph;
import main.java.com.extract.processor.model.MText;
import main.java.com.extract.processor.utils.WordUtils;


import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


public class WordUtils {

	// FIXME need to establish baseline font size, then establish HX based on what is found in the document
    public static final int HEADER_1_SIZE = 20;
    public static final int HEADER_2_SIZE = 18;
    public static final int HEADER_3_SIZE = 16;
    public static final int HEADER_4_SIZE = 14;
    
    // this is just an initial, reset at the end
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
    
    public static boolean isHeader(int defSize, int styleFontSize) {
        if (styleFontSize == -1) return false;
        if (styleFontSize > defSize) return true;
        return styleFontSize >= HEADER_4_SIZE;
    }

    public static int getStyleFontSize(XWPFDocument doc, XWPFParagraph wordParagraph) {
        XWPFStyle style = doc.getStyles().getStyle(wordParagraph.getStyleID());
        //log.debug("process style: " + wordParagraph.getStyleID());
        if (style != null) {
            CTRPr ctrPr = style.getCTStyle().getRPr();
            if (ctrPr != null) {
                CTHpsMeasure ctHpsMeasure = ctrPr.getSz();
                if (ctHpsMeasure != null) {
                    //log.debug("kern value: " + ctHpsMeasure.getVal());
                    BigInteger val = ctHpsMeasure.getVal();
                    return val.intValue()/2; // correct to get value
                } else {
                    //log.debug("measure was not found");
                }
            } else {
                //log.debug("pr was not found");
            }
        } else {
            //log.debug("paragraph doesn't styled");
        }
        return -1;
    }
    public static int getStyleFontSize(XWPFDocument doc, XWPFTable wordTable) {
    	XWPFTableRow row = wordTable.getRow(0);
    	XWPFTableCell cell = row.getCell(0);
    	//XWPFTableCell lcell = row.getCell(row.getTableCells().size()-1);
    	XWPFParagraph cellParagraph = cell.getParagraphArray(0);
    	return getStyleFontSize(doc, cellParagraph);
    }

    public static MParagraph processParagraph(XWPFParagraph wordParagraph) {
        MParagraph paragraph = new MParagraph();
        List<MText> textList = new ArrayList<>();
        for (XWPFRun run : wordParagraph.getRuns()) {
            MText text = new MText();
            text.setBold(run.isBold());
            text.setItalic(run.isItalic());
            text.setUnderlined(run.getUnderline() != UnderlinePatterns.NONE);
            text.setText(run.text());
            textList.add(text);
        }
        paragraph.setTexts(textList);
        return paragraph;
    }

    
    public static MParagraph processTable(XWPFTable table) {
        MParagraph paragraph = new MParagraph();
        List<MText> textList = new ArrayList<>();
        MText text = new MText();
        text.setText(table.getText());
        textList.add(text);
        paragraph.setTexts(textList);
        // would be nice to get text attributes.. but I don't see a way 
        return paragraph;
    }
    public static MHeader processTableHeader(XWPFTable table, int styleFontSize) {
        MHeader header = new MHeader();
        header.setLevel(getLevelByFontSize(styleFontSize));
        header.setFontSize(styleFontSize);
        header.setText(table.getText());
        return header;
    }

    public static MHeader processHeader(XWPFParagraph wordParagraph, int styleFontSize) {
        MHeader header = new MHeader();
        header.setLevel(getLevelByFontSize(styleFontSize));
        header.setFontSize(styleFontSize);
        header.setText(wordParagraph.getParagraphText());
        //System.out.println("     header["+header.getLevel()+"]["+header.getText()+"]: ");
        return header;
    }

    public static IBodyElement processList(Iterator<IBodyElement> iterator, XWPFParagraph firstElement, List<MElement> elements) {
        MList firstHtmlList = new MList();
        firstHtmlList.setElementList(new ArrayList<MListElement>());
        firstHtmlList.setSorted(!firstElement.getNumFmt().equals("bullet"));

        MListElement firstListElement = new MListElement();
        firstListElement.setTextList(processParagraph(firstElement).getTexts());

        firstHtmlList.getElementList().add(firstListElement);

        Stack<MList> htmlLists = new Stack<>();
        htmlLists.push(firstHtmlList);

        BigInteger currentLevel = firstElement.getNumIlvl();
       // System.out.println("  List["+currentLevel+"]["+firstElement.getText()+"]: ");
        IBodyElement unused = null;
        while (iterator.hasNext()) {
            IBodyElement element = iterator.next();
            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                BigInteger level = paragraph.getNumIlvl();
                if (level == null) {
                    //log.debug("end of list");
                   // System.out.println("     List END 1["+paragraph.getText()+"]: ");
                    unused = element;
                    break;
                }
                if (level.compareTo(currentLevel) > 0) {
                    //log.debug("element nested");
                    currentLevel = level;

                    MList htmlList = new MList();
                    htmlList.setSorted(!paragraph.getNumFmt().equals("bullet"));
                    htmlList.setElementList(new ArrayList<MListElement>());
                    htmlLists.peek().getElementList().get(htmlLists.peek().getElementList().size() - 1)
                            .setNestedList(htmlList);

                    htmlLists.push(htmlList);

                    MListElement listElement = new MListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);

                } else if (level.compareTo(currentLevel) < 0) {
                    //log.debug("end of nested list");
                    currentLevel = level;

                    htmlLists.pop();

                    MListElement listElement = new MListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);

                } else {
                    //log.debug("next list element");

                    MListElement listElement = new MListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);
                }
            } else {
                //log.debug("end of list");
                unused = element;
                break;
            }
        }
        // add it here
        elements.add(firstHtmlList);
        // return
        return unused;
    }
}
