package org.sedro.yadoc.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sedro.yadoc.model.MDocument;
import org.sedro.yadoc.model.MElement;
import org.sedro.yadoc.utils.HtmlUtils;
import org.sedro.yadoc.utils.SimpleHtmlUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ParseHtml implements YaParser {

    private static final String TYPE = "html";
    private String fileName;

    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public MDocument parseData(InputStream is, OutputStream os) throws Exception {

        MDocument simpleHtml = new MDocument();
        simpleHtml.setType(TYPE);
        simpleHtml.setHeaderList(new ArrayList<MElement>());
        simpleHtml.setElementList(new ArrayList<MElement>());
        simpleHtml.setFooterList(new ArrayList<MElement>());
        simpleHtml.setName(fileName);

        String in = readStream(is);
        Document document = Jsoup.parse(in);
        //Document document = Jsoup.parse(is, "UTF-8", "");
        HtmlUtils.processMeta(document, simpleHtml);
        HtmlUtils.simplify(document, simpleHtml);

        SimpleHtmlUtils.clearSimpleHtml(simpleHtml);
        SimpleHtmlUtils.optimizeSimpleHtml(simpleHtml);

        return simpleHtml;
    }

}
