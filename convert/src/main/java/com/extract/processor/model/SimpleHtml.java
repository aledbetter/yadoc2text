package com.extract.processor.model;

import lombok.Data;

import java.util.List;

@Data
public class SimpleHtml {
    private String title;
    private String type;
    private String name;
    private String created;
    private String modified;
    private List<Element> headerList;
    private List<Element> elementList;
    private List<Element> footerList;
}
