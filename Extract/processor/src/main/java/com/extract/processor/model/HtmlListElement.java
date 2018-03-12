package com.extract.processor.model;

import lombok.Data;

import java.util.List;

@Data
public class HtmlListElement {
    private List<Text> textList;
    private HtmlList nestedList;
}
