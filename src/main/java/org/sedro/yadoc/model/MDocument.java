package org.sedro.yadoc.model;


import java.util.List;

public class MDocument {
    private String title;
    private String type;
    private String name;
    private String created;
    private String modified;
    private String language;
    private String url;
    private List<MElement> headerList;
    private List<MElement> elementList;
    private List<MElement> footerList;
    
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<MElement> getHeaderList() {
		return headerList;
	}
	public void setHeaderList(List<MElement> headerList) {
		this.headerList = headerList;
	}
	public List<MElement> getElementList() {
		return elementList;
	}
	public void setElementList(List<MElement> elementList) {
		this.elementList = elementList;
	}
	public List<MElement> getFooterList() {
		return footerList;
	}
	public void setFooterList(List<MElement> footerList) {
		this.footerList = footerList;
	}
}
