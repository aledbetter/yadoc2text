package com.extract.processor.model;

import lombok.Data;

import java.util.List;

@Data
public class Paragraph implements Element {
    private List<Text> texts;
}
