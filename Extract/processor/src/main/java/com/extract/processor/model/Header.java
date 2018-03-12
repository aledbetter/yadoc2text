package com.extract.processor.model;

import lombok.Data;

@Data
public class Header implements Element {
    private int level;
    private String text;
}
