package com.extract.processor.parse;

import com.extract.processor.model.Element;
import com.extract.processor.model.SimpleHtml;
import com.extract.processor.render.SimpleHtmlRender;
import com.extract.processor.utils.HtmlUtils;
import com.extract.processor.utils.SimpleHtmlUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

@Log4j2
public class Html2Html implements Converter {

    private static final String TYPE = "html";

    @Getter
    @Setter
    private String fileName;

    @Override
    public void convert(InputStream is, OutputStream os) throws IOException {

        SimpleHtml simpleHtml = new SimpleHtml();
        simpleHtml.setType(TYPE);
        simpleHtml.setHeaderList(new ArrayList<Element>());
        simpleHtml.setElementList(new ArrayList<Element>());
        simpleHtml.setFooterList(new ArrayList<Element>());
        simpleHtml.setName(fileName);

        String in = readStream(is);
        Document document = Jsoup.parse(in);
        //Document document = Jsoup.parse(is, "UTF-8", "");
        HtmlUtils.processMeta(document, simpleHtml);
        HtmlUtils.simplify(document, simpleHtml);

        SimpleHtmlUtils.clearSimpleHtml(simpleHtml);
        SimpleHtmlUtils.optimizeSimpleHtml(simpleHtml);

        os.write(SimpleHtmlRender.render(simpleHtml).getBytes());
    }
	public static String readStream(InputStream input) {	
		// load the data to a buffer to prevent closed stream issue
		ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
		int read = 0;
		byte[] bytes = new byte[4096];
		try {
			while ((read = input.read(bytes)) != -1) {
				bOutput.write(bytes, 0, read);
			}
		} catch (IOException e) {
			return null;
		}
		//return bOutput.toString("UTF-8");
		return bOutput.toString();
	}

}
