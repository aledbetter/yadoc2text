package com.extract.processor.utils;

import com.extract.processor.model.*;
import lombok.extern.log4j.Log4j2;
import com.extract.processor.model.Header;
import com.extract.processor.model.HtmlList;
import com.extract.processor.model.HtmlListElement;
import com.extract.processor.model.Paragraph;
import com.extract.processor.model.SimpleHtml;
import com.extract.processor.model.Text;
import com.extract.processor.utils.HtmlUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class HtmlUtils {

    public static final Set<String> textTags;
    public static final Set<String> styleTags;
    public static final Set<String> listTags;
    public static final Set<String> headerTags;
    public static final Set<String> ignoredTags;
    public static final Set<String> ignoredRoles;
    public static final Set<String> containerTags;
    public static final Set<String> ignoreClassPartial;

    private static final Pattern headerPattern = Pattern.compile("^h([123456])$");

    static {
        textTags = new HashSet<>();
        textTags.add("p");
        textTags.add("a");
        textTags.add("span");
        styleTags = new HashSet<>();
        styleTags.add("b");
        styleTags.add("i");
        styleTags.add("u");
        styleTags.add("strong");
        styleTags.add("em");
        listTags = new HashSet<>();
        listTags.add("ul");
        listTags.add("ol");
        headerTags = new HashSet<>();
        headerTags.add("h1");
        headerTags.add("h2");
        headerTags.add("h3");
        headerTags.add("h4");
        headerTags.add("h5");
        headerTags.add("h6");
        ignoredTags = new HashSet<>();
        ignoredTags.add("script");
        ignoredTags.add("style");
        ignoredTags.add("meta");
        ignoredTags.add("link");
        ignoredTags.add("img");
        ignoredTags.add("noscript");
        ignoredTags.add("svg");
        ignoredTags.add("button");
        ignoredTags.add("input");
        ignoredTags.add("nav");
        ignoredTags.add("annotation");
        ignoredRoles = new HashSet<>();
        ignoredTags.add("menu");
        ignoredTags.add("menuitem");
        ignoredTags.add("menubar");
        ignoredTags.add("navigation");
        containerTags = new HashSet<>();
        containerTags.add("div");
        containerTags.add("nav");
        containerTags.add("section");
        containerTags.add("article");
        containerTags.add("iframe");
    	ignoreClassPartial = new HashSet<>();
    	ignoreClassPartial.add("button");
    	ignoreClassPartial.add("nav-");
    	ignoreClassPartial.add("-nav");
    }


    public static void simplify(Element element, SimpleHtml simpleHtml) {
        for (Element child : element.children()) {
            String tagName = child.tagName();
            String roleName = child.attr("role");
            String idName = child.attr("id");

            if (!isIgnored(child)) {
        		if (headerTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processHeader(child));
                    } else {
                        simpleHtml.getFooterList().add(processHeader(child));
                    }
                } else if (tagName.equals("title")) {
                    simpleHtml.setTitle(child.text());
                } else if (tagName.equals("header") || (roleName != null && roleName.equals("header"))) {
                    simplify(child, simpleHtml.getHeaderList());
                } else if (tagName.equals("footer") 
                		|| (roleName != null && roleName.equals("footer"))
                		|| (idName != null && idName.equals("footer"))) {
                	simplify(child, simpleHtml.getFooterList());
                } else if (listTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processList(child, simpleHtml.getElementList()));
                    } else {
                        simpleHtml.getFooterList().add(processList(child, simpleHtml.getFooterList()));
                    }
                } else if (textTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processParagraph(child));
                    } else {
                        simpleHtml.getFooterList().add(processParagraph(child));
                    }
                } else if (tagName.equals("div") && isDivText(child)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processParagraph(child));
                    } else {
                        simpleHtml.getFooterList().add(processParagraph(child));
                    }
                } else {
                    simplify(child, simpleHtml);
                }
            }
        }
    }

    public static void simplify(Element element, List<com.extract.processor.model.Element> elements) {   	
        for (Element child : element.children()) {
            String tagName = child.tagName();

            if (!isIgnored(child)) {
        		if (headerTags.contains(tagName)) {              
                    elements.add(processHeader(child));
                } else if (listTags.contains(tagName)) {
                    elements.add(processList(child, elements));
                } else if (textTags.contains(tagName)) {        
                     elements.add(processParagraph(child));
                } else if (tagName.equals("div") && isDivText(child)) {
                     elements.add(processParagraph(child));
                } else {
                     simplify(child, elements);             
                }
            }
        } 
    }

    public static Paragraph processParagraph(Element element) {
        Paragraph paragraph = new Paragraph();
        paragraph.setTexts(processText(element));
        return paragraph;
    }

    public static Header processHeader(Element element) {
        Header header = new Header();
        header.setLevel(getHeaderLevel(element.tagName()));
        header.setText(element.text());
        return header;
    }

    public static int getHeaderLevel(String headerTagName) {
        Matcher matcher = headerPattern.matcher(headerTagName);
        if (!matcher.matches()) {
            throw new RuntimeException("invalid header tag name: " + headerTagName);
        }
        return Integer.valueOf(matcher.group(1));
    }

    public static HtmlList processList(Element element, List<com.extract.processor.model.Element> elements) {

        HtmlList htmlList = new HtmlList();
        htmlList.setTextList(new ArrayList<Text>());
        htmlList.setElementList(new ArrayList<HtmlListElement>());

        for (Element child : element.children()) {
            String tagName = child.tagName();
            if (tagName.equals("li")) {
                htmlList.getElementList().add(processListElement(child, elements));
            } else if (tagName.startsWith("h") && tagName.length() == 2) {
                Matcher matcher = headerPattern.matcher(tagName);
                if (matcher.matches()) { // keep hX in the list as well
                	if (htmlList.getElementList() == null || htmlList.getElementList().size() < 1) {
                		// if first is header, place it before the list... common bad conceptual layout
                		Header hdr = processHeader(child);
                		elements.add(hdr);
                	} else {
                    	int hdr_level = getHeaderLevel(tagName);
                    	HtmlListElement hle = processListElement(child, elements);
                    	hle.setLevel(hdr_level);
                		htmlList.getElementList().add(hle);
                	}
                } else {
                    htmlList.getTextList().addAll(processText(child));
                }
            } else {
                htmlList.getTextList().addAll(processText(child));
            }
        }
        return htmlList;
    }

    public static HtmlListElement processListElement(Element element, List<com.extract.processor.model.Element> elements) {
        HtmlListElement result = new HtmlListElement();
        result.setTextList(new ArrayList<Text>());    
        result.setTagName(element.tagName().toLowerCase());

        if (containsOnlyFormattedText(element)) {
            result.setTextList(processText(element));
            return result;
        }

        for (Element child : element.children()) {
            String tagName = child.tagName();
            if (listTags.contains(tagName)) {
                result.setNestedList(processList(child, elements));
            } else {
                result.getTextList().addAll(processText(child));
            }
        }

        return result;
    }

    public static List<Text> processText(Element element) {
        List<Text> result = new ArrayList<>();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
            	String txt = ((TextNode) node).text();
            	if (element.tagName().equals("div") && txt.trim().isEmpty()) continue; // no empty div text
                Text text = new Text();
                text.setText(txt);
                text.setBold(isBold(node));
                text.setItalic(isItalic(node));
                text.setUnderlined(isUnderlined(node));
                result.add(text);
            	//System.out.println(" TEXT["+element.tagName()+"][" + text.getText()+"]");

            } else if (node instanceof Element) {
                result.addAll(processText((Element) node));
            }
        }
        return result;
    }
    
    // if contains text OR contains textElements
    public static boolean isDivText(Element element) {
    	int cnt1 = 0, cnt2 = 0, cntt = 0;
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
            	String txt = ((TextNode) node).text();
                if (!txt.trim().isEmpty()) {
                	// if it contains text, treat it like a span...
                	//System.out.println("DIV["+txt.length()+"] [" + txt+"]");
                    return true;
                }
                cnt1++;
            } else if (node instanceof Element) {
            	String tn = ((Element)node).tagName();
            	if (!tn.equalsIgnoreCase("p") && textTags.contains(tn)) {
                	cnt2++;
            	} 
            } else {
            	return false;
            }
            cntt++;
        }
        if ((cnt1 + cnt2) == cntt) return true;
        return false;
    }

    public static boolean containsOnlyFormattedText(Element element) {
        if (element.hasText()) {
            for (Element child : element.getAllElements()) {
                if (element != child) {
                    String tagName = child.tagName();
                    if (!styleTags.contains(tagName)) {
                        if (!containerTags.contains(tagName) && !tagName.equals("div")) {
                            if (!textTags.contains(tagName)) {
                                if (!headerTags.contains(tagName)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBold(Node node) {
        return (isStyled(node, "b") || isStyled(node, "strong"));
    }

    public static boolean isItalic(Node node) {
        return (isStyled(node, "i") || isStyled(node, "em"));
    }

    public static boolean isUnderlined(Node node) {
        return isStyled(node, "u");
    }

    public static boolean isStyled(Node node, String styleTagName) {
        Element container = null;
        if (node instanceof TextNode) {
            container = (Element) node.parent();
        } else if (node instanceof Element) {
            container = (Element) node;
        }
        if (container != null) {
            if (container.tagName().equals(styleTagName)) {
                return true;
            }
            for (Element parent : container.parents()) {
                if (parent.tagName().equals(styleTagName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isIgnored(Element element) {
        String tagName = element.tagName();
        if (ignoredTags.contains(tagName)) return true;
        String roleName = element.attr("role");
        if (roleName != null && ignoredRoles.contains(roleName)) return true;        
        String idName = element.attr("id");
        if (idName != null && idName.equals("nav")) return true;
        String className = element.attr("class");
        for (String s:ignoreClassPartial) {
        	if (className.contains(s)) return true;
        }
        return false;
    }
    
    public static void processMeta(Document document, SimpleHtml simpleHtml) {
  	   // <html lang="es">
   	   Element tlan = document.select("html").first();
  	   String lang = tlan.attr("lang");
        //<meta http-equiv="content-language" content="es">
  	   if (lang == null) lang = getMeta(document, "http-equiv=content-language");
  	   //<meta name="language" content="es">
 	   if (lang == null) lang = getMeta(document, "name=language");
 	   if (lang != null) {
 		   simpleHtml.setLanguage(lang.toLowerCase());
 	   }
 	   
 	   // <meta itemprop="url" content="https://www/...">
 	   String url = getMeta(document, "name=url");
 	   if (url == null) url = getMeta(document, "itemprop=url");
 	   if (url != null) {
 		   simpleHtml.setUrl(url);
 	   }
 	   // <meta content="2018-03-22T12:28:18Z" name="pubdate">
 	   String created = getMeta(document, "name=created");
 	   if (created == null) created = getMeta(document, "name=pubdate");
 	   if (created == null) created = getMeta(document, "name=Date");
 	   if (created != null) {
 		   simpleHtml.setCreated(created);
 	   }	 
  
 	   // <meta content="2018-03-22T13:44:08Z" name="lastmod">
 	   String modified = getMeta(document, "name=lastmod");
 	   if (modified == null) modified = getMeta(document, "name=Last-Modified");
 	   if (modified != null) {
 		   simpleHtml.setModified(modified);
 	   }	 
     }


    private static String getMeta(Document document, String match) {
  	   Elements el = document.select("meta["+match+"]");
  	   if (el == null) return null;
  	   Element fe = el.first();
  	   if (fe != null) return el.first().attr("content");
  	   return null;
     }

}
