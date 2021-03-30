package org.sedro.yadoc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.sedro.yadoc.model.*;


public class SimpleHtmlUtils {

    public static void clearSimpleHtml(MDocument simpleHtml) {
        if (simpleHtml.getHeaderList() != null) {
            clearElements(simpleHtml.getHeaderList());
        }
        clearElements(simpleHtml.getElementList());
        if (simpleHtml.getFooterList() != null) {
            decreaseHeaders(simpleHtml.getFooterList());
            clearElements(simpleHtml.getFooterList());
        }
    }

    public static void decreaseHeaders(List<MElement> elements) {
        for (MElement element : elements) {
            if (element instanceof MHeader) {
                MHeader header = (MHeader) element;
                if (header.getLevel() < 6) {
                    header.setLevel(header.getLevel() + 1);
                }
            }
        }
    }

    public static void clearElements(List<MElement> elements) {
        Iterator<MElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            MElement element = iterator.next();
            if (element instanceof MParagraph) {
                MParagraph paragraph = (MParagraph) element;
                clearText(paragraph.getTexts());
                if (paragraph.getTexts().isEmpty()) {
                    iterator.remove();
                }
            } else if (element instanceof MList) {
                MList htmlList = (MList) element;
                if (clearList(htmlList)) {
                    iterator.remove();
                }
            } else if (element instanceof MHeader) {
                MHeader header = (MHeader) element;
                if (header.getText().trim().isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    public static void clearText(List<MText> texts) {
        Iterator<MText> iterator = texts.iterator();
        while (iterator.hasNext()) {
            MText text = iterator.next();
            if (text.getText().equals("\n")) {
            	text.setText("  ");
            } else if (text.getText().trim().isEmpty()) {          	
                iterator.remove();
            }
        }
    }

    public static boolean clearList(MList htmlList) {
        boolean listIsEmpty = false;
        if (htmlList.getElementList() != null) {
            Iterator<MListElement> iterator = htmlList.getElementList().iterator();
            while (iterator.hasNext()) {
                MListElement htmlListElement = iterator.next();
                if (clearListItem(htmlListElement)) {
                    iterator.remove();
                }
            }
        }
        if (htmlList.getTextList() == null || htmlList.getTextList().isEmpty()) {
            if (htmlList.getElementList() == null || htmlList.getElementList().isEmpty()) {
                listIsEmpty = true;
            }
        }
        return listIsEmpty;
    }

    public static boolean clearListItem(MListElement htmlListElement) {
        boolean listElementIsEmpty = false;
        clearText(htmlListElement.getTextList());
        if (htmlListElement.getTextList() == null || htmlListElement.getTextList().isEmpty()) {
            if (htmlListElement.getNestedList() == null) {
                listElementIsEmpty = true;
            }
        }
        if (!listElementIsEmpty) {
            if (htmlListElement.getNestedList() != null) {
                if (clearList(htmlListElement.getNestedList())) {
                    htmlListElement.setNestedList(null);
                }
            }
        }
        return listElementIsEmpty;
    }

    public static void optimizeSimpleHtml(MDocument simpleHtml) {   	
    	optimizeSimpleHtmlList(simpleHtml.getHeaderList());
    	optimizeSimpleHtmlList(simpleHtml.getElementList());
    	optimizeSimpleHtmlList(simpleHtml.getFooterList());
    }
    
    // merge adjacent text together for lists and paragraphs
    private static void optimizeSimpleHtmlList(List<MElement> list) {
    	if (list == null || list.size() < 1) return;
    	
    	 // FIXME prevent headers from getting too long.. such as entire body   	
    	 // FIXME update headers vs text, merge into paragraphs? 	
    	
    	
    	
        for (MElement element : list) {
            if (element instanceof MParagraph) {
                MParagraph paragraph = (MParagraph) element;
                paragraph.setTexts(optimizeTexts(paragraph.getTexts()));
            } else  if (element instanceof MList) {
                MList htmlList = (MList) element;
                optimizeList(htmlList);
            }
        }
    }

   // merge adjacent text together in each list itme
   public static void optimizeList(MList htmlList) {
        if (htmlList == null) return;
        
        for (MListElement htmlListElement : htmlList.getElementList()) {
            htmlListElement.setTextList(optimizeTexts(htmlListElement.getTextList()));
            optimizeList(htmlListElement.getNestedList());
        }
    }

    // merge adjacent text together
    public static List<MText> optimizeTexts(List<MText> texts) {
        if (texts == null || texts.size() < 1) return texts;
        
        MText anchorText = texts.get(0);
        String last = anchorText.getText();
        int anchorIndex = 0;
        List<MText> result = new ArrayList<>();
        
        for (int i = 1; i < texts.size(); i++) {
            MText currentText = texts.get(i);
            if (checkSimilarFontParams(anchorText, currentText)) {
            	if (addSpace(last, currentText.getText())) {
                    //System.out.println(" NEEDT["+currentText.getText()+"] " + last);
                    currentText.setText(currentText.getText() + " ");
            	}
            	last = currentText.getText();
                continue;
            }
            result.add(joinTexts(texts.subList(anchorIndex, i)));
            anchorText = currentText;
            anchorIndex = i;
        }
        result.add(joinTexts(texts.subList(anchorIndex, texts.size())));
        return result;
    }

    public static MText joinTexts(List<MText> texts) {
        MText result = new MText();
        StringBuilder textContent = new StringBuilder();
        String last = null;
        for (MText text : texts) {
        	// make a space between tokens
        	if (addSpace(last, text.getText())) textContent.append(" ");
            last = text.getText();
            textContent.append(text.getText());
            result.setItalic(text.isItalic());
            result.setBold(text.isBold());
            result.setUnderlined(text.isUnderlined());           
        }
        result.setText(textContent.toString());
        return result;
    }
    
    public static boolean checkSimilarFontParams(MText text1, MText text2) {
        return (text1.isItalic() == text2.isItalic())
                && (text1.isBold() == text2.isBold())
                && (text1.isUnderlined() == text2.isUnderlined());
    }
    private static boolean addSpace(String first, String second) {
    	if (first != null && first.length() > 1 && second.length() > 1 && !first.endsWith(" ") && !second.startsWith(" ")) return true;
    	return false;
    }
    
    public static String cleanTexts(String text) {
    	if (text == null) return null;
    	//U+20000, represented by 2 chars in java (UTF-16 surrogate pair)
    	//text = text.replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", "");
    	return text.replace("\t", "    ").replace('—', '-').replace('–', '-').replace('‘', '\'')
    			.replace('“', '\"').replace('’', '\'').replace('”', '\"').replace('•', '.')
    			.replace("�", "--").replace("»", ">>").replace("Â", "").replace("â", "")
    			.replace("\\u0000", ""); // replace null chars (geting in doc/pdf from time to time)
    }	
    
    
    // eval levels and update based on new
    public static int evalHdrs(HashMap<Integer, MStyle> hmap, int fontSize, boolean isBold, int textHeight) {
    	if (hmap.size() < 1) return -1;
    	int nlv = -1;
    	MStyle max = null; // find most words (normal)
    	
    	// scan for where this size fits
    	for (Integer fsz:hmap.keySet()) {
    		MStyle hm = hmap.get(fsz);
    		if (max == null || max.getCount_char() < hm.getCount_char()) {
    			max = hm;
    	    //	System.out.println("   norm["+max.count_char+"] TXT: " + max.ftxtp.getFontSize());
    		}
            
    		if (fsz > fontSize) {
    			if (nlv == -1 || nlv <= hm.getLevel()) {
    				nlv = hm.getLevel()+1;
    		    //	System.out.println("    Tst_0["+nlv+"]["+max.level+"] " + max.ftxtp.getFontSize());
    			}   			
    		} else if (fsz < fontSize) {
    			// bigger
    			if (nlv == -1 || nlv >= hm.getLevel()) {
    				nlv = hm.getLevel()-1;
    		  //  	System.out.println("    Tst_1["+nlv+"]["+max.level+"] " + max.ftxtp.getFontSize());
    			}
    		} else {
    			return hm.getLevel();
    		}
    	}
// FIXME reset those that are off
 //   	System.out.println("  Tst["+nlv+"]["+max.getLevel()+"] ");
    	return nlv;
    }
    
// FIXME correct all headers method -> this should be in HTML to work for all on end
    // move some to text...
    public static void mapHedersAndText(MDocument simpleHtml) {
    	// correct MStyle
    	// FIXME
    	
    	HashMap<Integer, MStyle> smap = simpleHtml.getStyleSet();
    	
    	// check and update all 
    	for (MElement el:simpleHtml.getElementList()) {
    		if (el instanceof MHeader) {
    			MHeader hdr = (MHeader) el;
    			MStyle hm = smap.get(hdr.getFontSize());
    			if (hm.getLevel() != hdr.getLevel()) {
    				if (hm.getLevel() < 1) {
    					// make text
 //FIXME   					
    				} else {
    					// update
    					hdr.setLevel(hm.getLevel());
    				}
    			}
    		}
    	}   	   	
    }
    
    
}
