package com.extract.processor.model;

import lombok.Data;

@Data
public class Text {
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private String text;
}
