package com.extract.processor.utils;

import com.extract.processor.model.Element;
import com.extract.processor.model.Header;
import com.extract.processor.model.HtmlList;
import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Paragraph;
import com.extract.processor.model.Text;
import com.extract.processor.utils.WordUtils;

import lombok.extern.log4j.Log4j2;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

@Log4j2
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
        log.debug("process style: " + wordParagraph.getStyleID());
        if (style != null) {
            CTRPr ctrPr = style.getCTStyle().getRPr();
            if (ctrPr != null) {
                CTHpsMeasure ctHpsMeasure = ctrPr.getSz();
                if (ctHpsMeasure != null) {
                    log.debug("kern value: " + ctHpsMeasure.getVal());
                    BigInteger val = ctHpsMeasure.getVal();
                    return val.intValue()/2; // correct to get value
                } else {
                    log.debug("measure was not found");
                }
            } else {
                log.debug("pr was not found");
            }
        } else {
            log.debug("paragraph doesn't styled");
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

    public static Paragraph processParagraph(XWPFParagraph wordParagraph) {
        Paragraph paragraph = new Paragraph();
        List<Text> textList = new ArrayList<>();
        for (XWPFRun run : wordParagraph.getRuns()) {
            Text text = new Text();
            text.setBold(run.isBold());
            text.setItalic(run.isItalic());
            text.setUnderlined(run.getUnderline() != UnderlinePatterns.NONE);
            text.setText(run.text());
            textList.add(text);
        }
        paragraph.setTexts(textList);
        return paragraph;
    }

    
    public static Paragraph processTable(XWPFTable table) {
        Paragraph paragraph = new Paragraph();
        List<Text> textList = new ArrayList<>();
        Text text = new Text();
        text.setText(table.getText());
        textList.add(text);
        paragraph.setTexts(textList);
        // would be nice to get text attributes.. but I don't see a way 
        return paragraph;
    }
    public static Header processTableHeader(XWPFTable table, int styleFontSize) {
        Header header = new Header();
        header.setLevel(getLevelByFontSize(styleFontSize));
        header.setFontSize(styleFontSize);
        header.setText(table.getText());
        return header;
    }

    public static Header processHeader(XWPFParagraph wordParagraph, int styleFontSize) {
        Header header = new Header();
        header.setLevel(getLevelByFontSize(styleFontSize));
        header.setFontSize(styleFontSize);
        header.setText(wordParagraph.getParagraphText());
        //System.out.println("     header["+header.getLevel()+"]["+header.getText()+"]: ");
        return header;
    }

    public static IBodyElement processList(Iterator<IBodyElement> iterator, XWPFParagraph firstElement, List<Element> elements) {
        HtmlList firstHtmlList = new HtmlList();
        firstHtmlList.setElementList(new ArrayList<HtmlListElement>());
        firstHtmlList.setSorted(!firstElement.getNumFmt().equals("bullet"));

        HtmlListElement firstListElement = new HtmlListElement();
        firstListElement.setTextList(processParagraph(firstElement).getTexts());

        firstHtmlList.getElementList().add(firstListElement);

        Stack<HtmlList> htmlLists = new Stack<>();
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
                    log.debug("end of list");
                   // System.out.println("     List END 1["+paragraph.getText()+"]: ");
                    unused = element;
                    break;
                }
                if (level.compareTo(currentLevel) > 0) {
                    log.debug("element nested");
                    currentLevel = level;

                    HtmlList htmlList = new HtmlList();
                    htmlList.setSorted(!paragraph.getNumFmt().equals("bullet"));
                    htmlList.setElementList(new ArrayList<HtmlListElement>());
                    htmlLists.peek().getElementList().get(htmlLists.peek().getElementList().size() - 1)
                            .setNestedList(htmlList);

                    htmlLists.push(htmlList);

                    HtmlListElement listElement = new HtmlListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);

                } else if (level.compareTo(currentLevel) < 0) {
                    log.debug("end of nested list");
                    currentLevel = level;

                    htmlLists.pop();

                    HtmlListElement listElement = new HtmlListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);

                } else {
                    log.debug("next list element");

                    HtmlListElement listElement = new HtmlListElement();
                    listElement.setTextList(processParagraph(paragraph).getTexts());
                    htmlLists.peek().getElementList().add(listElement);
                }
            } else {
                log.debug("end of list");
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
