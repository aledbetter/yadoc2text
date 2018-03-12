package com.extract.processor.model;

import lombok.Data;

import java.util.List;

@Data
public class HtmlList implements Element {
    private List<Text> textList;
    private List<HtmlListElement> elementList;
    private boolean sorted;
}
