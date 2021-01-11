package main.java.com.extract.processor.parse;

import main.java.com.extract.processor.model.MElement;
import main.java.com.extract.processor.model.SimpleHtml;
import main.java.com.extract.processor.render.SimpleHtmlRender;
import main.java.com.extract.processor.utils.HtmlUtils;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
    public void convert(InputStream is, OutputStream os) throws IOException {

        SimpleHtml simpleHtml = new SimpleHtml();
        simpleHtml.setType(TYPE);
        simpleHtml.setHeaderList(new ArrayList<MElement>());
        simpleHtml.setElementList(new ArrayList<MElement>());
        simpleHtml.setFooterList(new ArrayList<MElement>());
        simpleHtml.setName(fileName);

        String in = readStream(is);
        System.out.println("FIXME: rtf not supported ");


        os.write(in.getBytes());
    }
	

}
