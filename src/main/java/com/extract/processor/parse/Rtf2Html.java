package main.java.com.extract.processor.parse;

import main.java.com.extract.processor.model.MElement;
import main.java.com.extract.processor.model.SimpleHtml;
import main.java.com.extract.processor.render.SimpleHtmlRender;
import main.java.com.extract.processor.utils.HtmlUtils;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;
import main.java.com.extract.processor.utils.rtf.RtfHtml;
import main.java.com.extract.processor.utils.rtf.RtfParseException;
import main.java.com.extract.processor.utils.rtf.RtfReader;

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
public class Rtf2Html implements Converter2Html {

    private static final String TYPE = "rtf";

    private String fileName;

    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
    public SimpleHtml convertData(InputStream is, OutputStream os) throws Exception {
        SimpleHtml simpleHtml = new SimpleHtml();
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
