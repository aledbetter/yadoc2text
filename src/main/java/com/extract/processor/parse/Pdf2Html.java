package com.extract.processor.parse;

import com.extract.processor.model.SimpleHtml;
import com.extract.processor.render.SimpleHtmlRender;
import com.extract.processor.utils.SimpleHtmlUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.Stripper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Log4j2
public class Pdf2Html implements Converter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
    private static final String TYPE = "pdf";

    @Getter
    @Setter
    private String fileName;

    @Override
    public void convert(InputStream is, OutputStream os) throws Exception {
        PDDocument document = PDDocument.load(is);
        Stripper stripper = new Stripper();
        StringWriter stringWriter = new StringWriter();
        stripper.writeText(document, stringWriter);

        Calendar creation = document.getDocumentInformation().getCreationDate();
        Calendar modification = document.getDocumentInformation().getModificationDate();
       // document.getDocumentInformation().getCreator()
        log.debug("document name: " + fileName);
        log.debug("document creation: " + creation);
        log.debug("document modification: " + modification);

        SimpleHtml simpleHtml = stripper.getSimpleHtml();
        simpleHtml.setType(TYPE);
        simpleHtml.setName(fileName);
        if (creation != null) {
            simpleHtml.setCreated(sdf.format(creation.getTime()));
        }
        if (modification != null) {
            simpleHtml.setModified(sdf.format(modification.getTime()));
        }
        SimpleHtmlUtils.clearSimpleHtml(simpleHtml);
        SimpleHtmlUtils.optimizeSimpleHtml(simpleHtml);

        os.write(SimpleHtmlRender.render(simpleHtml).getBytes());
    }
}
