package com.extract.processor.parse;

import com.extract.processor.model.Element;
import com.extract.processor.model.SimpleHtml;
import com.extract.processor.render.SimpleHtmlRender;
import com.extract.processor.utils.WordUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

@Log4j2
public class Word2Html implements Converter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
    private static final String TYPE = "word";

    @Getter
    @Setter
    private String fileName;

    @Override
    public void convert(InputStream is, OutputStream os) throws IOException, OpenXML4JException, XmlException {
        XWPFDocument doc = new XWPFDocument(is);

        OPCPackage pkg = doc.getPackage();
        POIXMLProperties props = new POIXMLProperties(pkg);
        PackagePropertiesPart ppropsPart = props.getCoreProperties().getUnderlyingProperties();

        Date created = ppropsPart.getCreatedProperty().getValue();
        Date modified = ppropsPart.getModifiedProperty().getValue();

        log.debug("document name: " + fileName);
        log.debug("document created: " + created);
        log.debug("document modified: " + modified);

        SimpleHtml simpleHtml = new SimpleHtml();
        simpleHtml.setType(TYPE);
        simpleHtml.setName(fileName);

        if (created != null) {
            simpleHtml.setCreated(sdf.format(created));
        }
        if (modified != null) {
            simpleHtml.setCreated(sdf.format(modified));
        }
        simpleHtml.setElementList(new ArrayList<Element>());

        SimpleHtmlRender simpleHtmlRender = SimpleHtmlRender.factoryMethod();

        Iterator<IBodyElement> iterator = doc.getBodyElementsIterator();
        while (iterator.hasNext()) {
            IBodyElement element = iterator.next();
            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                int styleFontSize = WordUtils.getStyleFontSize(doc, paragraph);
                if (paragraph.getRuns().size() > 0) {
                    if (doc.getNumbering() != null && doc.getNumbering().numExist(paragraph.getNumID())) {
                        log.debug("list was fount");
                        simpleHtml.getElementList().add(WordUtils.processList(iterator, paragraph));
                    } else if (WordUtils.isHeader(paragraph.getRuns().get(0).getFontSize(), styleFontSize)) {
                        log.debug("header was found");
                        simpleHtml.getElementList().add(WordUtils.processHeader(paragraph, styleFontSize));
                    } else {
                        log.debug("paragraph was found");
                        simpleHtml.getElementList().add(WordUtils.processParagraph(paragraph));
                    }
                }
            } else if (element instanceof XWPFTable) {
                log.debug("table was found");
                XWPFTable table = (XWPFTable) element;
                simpleHtml.getElementList().add(WordUtils.processTable(table));
            }
        }
        os.write(simpleHtmlRender.render(simpleHtml).getBytes());
    }
}
