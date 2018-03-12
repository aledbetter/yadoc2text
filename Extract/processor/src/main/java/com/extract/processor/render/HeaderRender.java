package com.extract.processor.render;

import com.extract.processor.model.Header;

public class HeaderRender {
    public String render(Header header) {
        return "<h" + header.getLevel() + ">" + header.getText() + "</h" + header.getLevel() + ">";
    }
}
