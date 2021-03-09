package main.java.com.yadoc.processor.parse;

import main.java.com.yadoc.processor.model.MDocument;
import main.java.com.yadoc.processor.model.MElement;
import main.java.com.yadoc.processor.render.DocumentRender;
import main.java.com.yadoc.processor.utils.HtmlUtils;
import main.java.com.yadoc.processor.utils.SimpleHtmlUtils;
import main.java.com.yadoc.processor.utils.rtf.RtfHtml;
import main.java.com.yadoc.processor.utils.rtf.RtfParseException;
import main.java.com.yadoc.processor.utils.rtf.RtfReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

//https://github.com/kschroeer/rtf-html-java
// saved
public class ParseRtf implements YaParser {

    private static final String TYPE = "rtf";

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
        RtfReader reader = new RtfReader();
        RtfHtml formatter = new RtfHtml();
        String fmt = null;
        try {
            reader.parse(in);
            fmt = formatter.format(reader.root, true);
        //    System.out.println(fmt);
        } catch (RtfParseException e) {
            e.printStackTrace();
        }       
        Document document = Jsoup.parse(fmt);
        //Document document = Jsoup.parse(is, "UTF-8", "");
        HtmlUtils.processMeta(document, simpleHtml);
        HtmlUtils.simplify(document, simpleHtml);

        SimpleHtmlUtils.clearSimpleHtml(simpleHtml);
        SimpleHtmlUtils.optimizeSimpleHtml(simpleHtml);
        return simpleHtml;       
	}

}