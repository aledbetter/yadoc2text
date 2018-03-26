package com.extract.processor.model;

import lombok.Data;
import com.extract.processor.model.Element;

import java.util.List;

@Data
public class SimpleHtml {
    private String title;
    private String type;
    private String name;
    private String created;
    private String modified;
    private String language;
    private String url;
    private List<Element> headerList;
    private List<Element> elementList;
    private List<Element> footerList;
}
