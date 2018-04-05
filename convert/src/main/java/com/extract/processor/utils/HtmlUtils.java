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

import com.extract.processor.render.TextRenderer;

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
    public static final Set<String> ignoreClass;

    private static final Pattern headerPattern = Pattern.compile("^h([123456])$");

    static {
        textTags = new HashSet<>();
        textTags.add("p");
        textTags.add("a");
        textTags.add("span");
        textTags.add("td");
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
        ignoredTags.add("label");
        ignoredTags.add("nav");
        ignoredTags.add("sup");
        ignoredTags.add("semantics");
        ignoredTags.add("math");
        
        ignoredTags.add("annotation");
    //    ignoredTags.add("figure");	// ?? not certain this is always ideal
        
        ignoredRoles = new HashSet<>();
        ignoredRoles.add("menu");
        ignoredRoles.add("menuitem");
        ignoredRoles.add("menubar");
        ignoredRoles.add("navigation");
        containerTags = new HashSet<>();
        containerTags.add("div");
        containerTags.add("nav");
        containerTags.add("section");
        containerTags.add("article");
        containerTags.add("table");
        containerTags.add("iframe");
        
        // heuristic based ignore for controls
    	ignoreClass = new HashSet<>();
    	ignoreClass.add("toc"); // wikipedia
    	ignoreClass.add("footer-places"); // wikipedia
    	ignoreClass.add("thumbcaption"); // wikipedia
    	ignoreClass.add("printfooter"); // wikipedia
    	ignoreClass.add("noprint"); // wikipedia
    	
    	ignoreClassPartial = new HashSet<>();
    	ignoreClassPartial.add("button");
    	ignoreClassPartial.add("-btn");
    	ignoreClassPartial.add("btn-");
    	ignoreClassPartial.add("-action");
    	ignoreClassPartial.add("carousel");

    	ignoreClassPartial.add("nav-");
    	ignoreClassPartial.add("-nav");
    	ignoreClassPartial.add("Nav");
    	
    	ignoreClassPartial.add("catlinks"); // wikipedia
    	ignoreClassPartial.add("reflist"); // wikipedia
    	ignoreClassPartial.add("-edit"); // wikipedia
   	
    	ignoreClassPartial.add("popover");
    	ignoreClassPartial.add("popup");
    	ignoreClassPartial.add("overlay");	

    	ignoreClassPartial.add("notification");

    	ignoreClassPartial.add("signup"); // ??	
    	ignoreClassPartial.add("login"); // ??	
    	ignoreClassPartial.add("signin"); // ??	
    	ignoreClassPartial.add("share-count"); // ??	general -count for counters?
    	
    	ignoreClassPartial.add("-replay");
    	ignoreClassPartial.add("replay-");    	
    	ignoreClassPartial.add("video");
    	//ignoreClassPartial.add("_header");
    	//ignoreClassPartial.add("header_");
    	ignoreClassPartial.add("tools");
    	ignoreClassPartial.add("caption");
    }

    public static void simplify(Element element, SimpleHtml simpleHtml) {
        for (Element child : element.children()) {
            if (!isIgnored(child)) {
                String tagName = child.tagName();
                String roleName = child.attr("role");
                String idName = child.attr("id");
                
        		if (headerTags.contains(tagName)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processHeader(child, simpleHtml.getElementList()));
                    } else {
                        simpleHtml.getFooterList().add(processHeader(child, simpleHtml.getFooterList()));
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
                        simpleHtml.getElementList().add(processParagraph(child, simpleHtml.getElementList()));
                    } else {
                        simpleHtml.getFooterList().add(processParagraph(child, simpleHtml.getFooterList()));
                    }
                } else if (tagName.equals("div") && isDivText(child)) {
                    if (simpleHtml.getFooterList().isEmpty()) {
                        simpleHtml.getElementList().add(processParagraph(child, simpleHtml.getElementList()));
                    } else {
                        simpleHtml.getFooterList().add(processParagraph(child, simpleHtml.getFooterList()));
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
                    elements.add(processHeader(child, elements));
                } else if (listTags.contains(tagName)) {
                    elements.add(processList(child, elements));
                } else if (textTags.contains(tagName)) {        
                     elements.add(processParagraph(child, elements));
                } else if (tagName.equals("div") && isDivText(child)) {
                     elements.add(processParagraph(child, elements));
                } else {
                     simplify(child, elements);             
                }
            }
        } 
    }

    public static Paragraph processParagraph(Element element, List<com.extract.processor.model.Element> elements) {
        Paragraph paragraph = new Paragraph();
        paragraph.setTexts(processText(element, elements));
        return paragraph;
    }

    public static Header processHeader(Element element, List<com.extract.processor.model.Element> elements) {
        Header header = new Header();
        header.setLevel(getHeaderLevel(element.tagName()));
        List<Text> tl = processText(element, elements);
        StringBuilder result = new StringBuilder();
        if (tl != null) {
            for (Text text : tl) {
                result.append(TextRenderer.render(text));
            }
        }
        header.setText(result.toString());
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
            if (isIgnored(child)) {
            	continue;
            } else if (tagName.equals("li")) {
                htmlList.getElementList().add(processListElement(child, elements));
            } else if (tagName.startsWith("h") && tagName.length() == 2) {
                Matcher matcher = headerPattern.matcher(tagName);
                if (matcher.matches()) { // keep hX in the list as well
                	if (htmlList.getElementList() == null || htmlList.getElementList().size() < 1) {
                		// if first is header, place it before the list... common bad conceptual layout
                		Header hdr = processHeader(child, elements);
                		elements.add(hdr);
                	} else {
                    	int hdr_level = getHeaderLevel(tagName);
                    	HtmlListElement hle = processListElement(child, elements);
                    	hle.setLevel(hdr_level);
                		htmlList.getElementList().add(hle);
                	}
                } else {
                    htmlList.getTextList().addAll(processText(child, elements));
                }
            } else {           	
                htmlList.getTextList().addAll(processText(child, elements));
            }
        }
        return htmlList;
    }

    public static HtmlListElement processListElement(Element element, List<com.extract.processor.model.Element> elements) {
        HtmlListElement result = new HtmlListElement();
        result.setTextList(new ArrayList<Text>());    
        result.setTagName(element.tagName().toLowerCase());

        if (containsOnlyFormattedText(element)) {
            result.setTextList(processText(element, elements));
            return result;
        }
        // ISSUE: some li contain multiple div/p, these each need to render in <p> 
        for (Element child : element.children()) {
            String tagName = child.tagName();
        	if (isIgnored(child)) {
        		continue;
        	} else if (listTags.contains(tagName)) {
                result.setNestedList(processList(child, elements));
            } else {
                result.getTextList().addAll(processText(child, elements));
            }
        }

        return result;
    }

    public static List<Text> processText(Element element, List<com.extract.processor.model.Element> elements) {
        List<Text> result = new ArrayList<>();
        boolean first = true;
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
            	if (isIgnored((Element) node)) continue;
            	if (((Element) node).tagName().equals("br")) {
                    Text text = new Text();
                    text.setText("\n");
                	//System.out.println(" ADD_BR["+element.tagName()+"][" + text.getText()+"]");
                    result.add(text);
            	} else if (first && ((Element) node).tagName().equals("cite")) { 
            		// perhapse others fit this as well?
            		elements.add(processParagraph((Element) node, elements));
            	} else {
            		result.addAll(processText((Element) node, elements));
            	}
            }
            first = false;
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
        if (ignoredTags.contains(tagName)) {
            //System.out.println("IGNC["+tagName+"]");
        	return true;
        }
        
        String roleName = element.attr("role");
        if (roleName != null && ignoredRoles.contains(roleName)) return true;     
        
        String idName = element.attr("id");
        if (idName != null && idName.equals("nav")) return true;
        
        String className = element.attr("class");
        if (className == null && idName == null) return false;
        //System.out.println("IGNC["+tagName+"] id: "+idName+" class: " + className);

        for (String s:ignoreClassPartial) {
        	if (className != null && className.contains(s)) return true;
        	if (idName != null && idName.contains(s)) return true;
        }
        for (String s:ignoreClass) {
        	if (className != null && className.equals(s)) return true;
        	if (idName != null && idName.equals(s)) return true;
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
 	   if (lang != null && !lang.isEmpty()) {
 		   simpleHtml.setLanguage(lang.toLowerCase());
 	   }
 	   
 	   // <meta itemprop="url" content="https://www/...">
 	   String url = getMeta(document, "name=url");
 	   if (url == null) url = getMeta(document, "itemprop=url");
 	   if (url != null && !url.isEmpty()) {
 		   simpleHtml.setUrl(url);
 	   }
 	   // <meta content="2018-03-22T12:28:18Z" name="pubdate">
 	   String created = getMeta(document, "name=created");
 	   if (created == null) created = getMeta(document, "name=pubdate");
 	   if (created == null) created = getMeta(document, "name=Date");
 	   if (created != null && !created.isEmpty()) {
 		   simpleHtml.setCreated(created);
 	   }	 
  
 	   // <meta content="2018-03-22T13:44:08Z" name="lastmod">
 	   String modified = getMeta(document, "name=lastmod");
 	   if (modified == null) modified = getMeta(document, "name=Last-Modified");
 	   if (modified != null && !modified.isEmpty()) {
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