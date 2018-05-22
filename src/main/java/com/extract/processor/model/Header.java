package com.extract.processor.model;

import lombok.Data;

@Data
public class Header implements Element {
    private int level;
    private int fontSize;
    private String text;
}
