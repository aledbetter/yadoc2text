package org.sedro.yadoc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.sedro.yadoc.model.*;


public class SimpleHtmlUtils {
	private static final int WRAP_CHARS = 79;
	private static final int WRAP_CHARS_TEST = 65;

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
    	mapHedersAndText(simpleHtml);
    	optimizeSimpleHtmlList(simpleHtml, simpleHtml.getHeaderList());
    	optimizeSimpleHtmlList(simpleHtml, simpleHtml.getElementList());
    	optimizeSimpleHtmlList(simpleHtml, simpleHtml.getFooterList());
    }
    
    // merge adjacent text together for lists and paragraphs
    private static void optimizeSimpleHtmlList(MDocument simpleHtml, List<MElement> list) {
    	if (list == null || list.size() < 1) return;
    	

		/*
		 * correct out of order..
		DRt[2][25]y[376] [Cisco Inc]
		HDRt[3][22]y[357] [Associate Recruiter]
		HDRt[6][17]y[378] [APR 2016 - SEPT 2020]        		
		 */	
    	MElement lem = null;
    	for (int i=0;i<list.size();i++) {
    		MElement element = list.get(i);
        	if (element instanceof MHeader) {
        		MHeader h = (MHeader)element;
        		if (lem != null && lem instanceof MText) {
        			MText t = (MText)lem;
        			if (t.getY() < (h.getY()-h.getFontSize()) && t.getY() > 0) { 
            			//System.out.println("HDR_order["+h.getLevel()+"]["+t.getFontSize()+"/"+t.getFontSize()+"]y["+h.getY()+" > "+t.getY()+"] ["+h.getText()+"][" + t.getText()+"]"); 
            			list.remove(i);
            			list.add(i-1, element);
            			i--;
        			}
        		}
        	}
        	lem = element;
    	}
    	
    	
        // merge headers
    	lem = null;
    	Iterator<MElement> it = list.iterator();
    	int lastLen = 0;
    	while (it.hasNext()) {
    		MElement element = it.next();
        	if (element instanceof MHeader) {
        		MHeader h = (MHeader)element;
        		if (lem != null && lem instanceof MHeader) {
        			MHeader t = (MHeader)lem;
        	    	// merge headings: use y to determine if headings should merge (same line)
        	// TODO: +/- X on same line (based on font size)
        			if (t.getY() == h.getY() && t.getY() > 0) {
    					// merge with last AS HEADING
                    	if (addSpace(t.getText(), h.getText())) {
                			t.setText(t.getText()+" "+h.getText());
                    	} else {
                			t.setText(t.getText()+h.getText());
                    	}
            			//System.out.println("HDR_m["+h.getLevel()+"]["+h.getFontSize()+"]y["+t.getY()+"/"+h.getY()+"] [" + t.getText()+"]");   
                    	t.setY(h.getY());
                    	it.remove();
                    	continue;				
        			} else if (t.getLevel() == 0 && h.getLevel() == 0) {
        				if (lastLen >= WRAP_CHARS) {
                			lastLen = h.getText().length();
    			
	        				// merge with last as HEADING that is text
	                    	if (addSpace(t.getText(), h.getText())) {
	                			t.setText(t.getText()+" "+h.getText());
	                    	} else {
	                			t.setText(t.getText()+h.getText());
	                    	}
	            			//System.out.println("HDR_0["+h.getLevel()+"]["+h.getFontSize()+"]y["+t.getY()+"/"+h.getY()+"] [" + t.getText()+"]");   
	            			t.setY(h.getY());
	            			it.remove();
	                    	continue;	
                    	    	
        				} else if (lastLen > WRAP_CHARS_TEST && (lastLen + h.getText().length()) > WRAP_CHARS) {
            				// wrap must check the first word in h.getText() to see if t.getText().length() + len >= WRAP_CHARS
        					int idx = h.getText().indexOf(' ');
        					if (idx < 0 || (idx > 0 && (lastLen + idx) > WRAP_CHARS)) {
                    			lastLen = h.getText().length();
                    			
    	        				// merge with last as HEADING that is text
    	                    	if (addSpace(t.getText(), h.getText())) {
    	                			t.setText(t.getText()+" "+h.getText());
    	                    	} else {
    	                			t.setText(t.getText()+h.getText());
    	                    	}
            					//System.out.println("HDR_0["+h.getLevel()+"]["+h.getFontSize()+"]y["+t.getY()+"/"+h.getY()+"]["+t.getText().length()+"] [" + t.getText()+"]");   
    	            			t.setY(h.getY());
    	            			it.remove();       						
    	                    	continue;	
        					}
        				}
            			lastLen = h.getText().length();
        			}
        		}
        	}
        	lem = element;
        } 	
    	
    	// setup lists and paragraphs
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
    
    
// FIXME correct all headers method -> this should be in HTML to work for all on end
    // move some to text...
    public static void mapHedersAndText(MDocument simpleHtml) {
    	HashMap<Integer, MStyle> smap = simpleHtml.getStyleSet();
    	if (smap.keySet().size() < 1) return;
    	
    	List<MStyle> ol = new ArrayList<>();
    	// Determine what is normal
    	MStyle normal = null; // find most words (normal)
    	for (Integer fsz:smap.keySet()) {
    		MStyle hm = smap.get(fsz);
    		if (normal == null || normal.getCount_char() < hm.getCount_char()) {
    			normal = hm;
    		}
    		// TODO use the text to set the info   		
        	//ms.setBold(ishdr);
        	//ms.setItalic(ishdr);
        	//ms.setUnderlined(ishdr);
        	//ms.setUpper(ishdr);
    		// add sorted by fontsize (short list so..)
    		int i=0;
    		for (;i<ol.size();i++) {
    			if (ol.get(i).getFontSize() <= hm.getFontSize()) {
    				break;
    			}
    		}
    		ol.add(i, hm);
    	}  
    	simpleHtml.setStyleNormal(normal);
    	//System.out.println("NORM["+normal.getFontSize()+"]");
    	
    	// update styles level for headings
    	for (int i=0;i<ol.size();i++) {
    		MStyle hm = ol.get(i);
		//	if (hm.getFontSize() <= normal.getFontSize() && !hm.isBold() && !hm.isItalic() && !hm.isUpper() && !hm.isUnderlined()) {
			if (hm.getFontSize() == normal.getFontSize()) {
		    	//System.out.println(" norm["+hm.getFontSize()+"]");
				hm.setLevel(0);
			} else if (hm.getFontSize() < normal.getFontSize()) {
				// smaller... ??
				hm.setLevel(i+1);
			} else {
		    	//System.out.println(" hdr["+hm.getFontSize()+"]["+i+"]");
				hm.setLevel(i+1);
			}
    	}  
    	
    	// check and update all 
    	for (MElement el:simpleHtml.getElementList()) {
    		if (el instanceof MHeader) {
    			MHeader hdr = (MHeader) el;
    			MStyle hm = smap.get(hdr.getFontSize());
    			if (hm.getLevel() != hdr.getLevel()) {
					hdr.setLevel(hm.getLevel());
    			}
    		}
    	}   	   	
    }
    
    
}
