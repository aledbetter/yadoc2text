package com.extract.processor.render;

import com.extract.processor.model.Text;

public class TextRenderer {
    public static String render(Text text) {

        StringBuilder prefix = new StringBuilder();
        StringBuilder suffix = new StringBuilder();

        if (text.isBold()) {
            prefix.append("<b>");
            suffix.insert(0, "</b>");
        }
        if (text.isItalic()) {
            prefix.append("<i>");
            suffix.insert(0, "</i>");
        }
        if (text.isUnderlined()) {
            prefix.append("<u>");
            suffix.insert(0, "</u>");
        }
        String txt = text.getText().replace("\t", "    "); // replace tabs always
        txt = txt.replace('—', '-').replace('–', '-').replace('‘', '\'').replace('“', '\"').replace('’', '\'')
        		.replace('”', '\"').replace('•', '.').replace("�", "--").replace("»", ">>").replace("Â", "")
        		.replace("\\u0000", ""); // replace null chars (geting in doc/pdf from time to time)
        if (txt.trim().length() > 0) { // fmt only if something to fmt
        	return prefix.toString() + txt + suffix.toString();
        }
        return txt;
    }
}
