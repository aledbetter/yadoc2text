package main.java.com.extract.processor.parse;

import main.java.com.extract.processor.model.SimpleHtml;
import main.java.com.extract.processor.render.SimpleHtmlRender;
import main.java.com.extract.processor.utils.SimpleHtmlUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.Stripper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//FIXME will this Stripper override the other? 

public class Pdf2Html implements ConverterHtml {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
    private static final String TYPE = "pdf";

    private String fileName;

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
    public void convert(InputStream is, OutputStream os) throws Exception {
        PDDocument document = PDDocument.load(is);
        Stripper stripper = new Stripper();
        StringWriter stringWriter = new StringWriter();
        stripper.writeText(document, stringWriter);

        Calendar creation = document.getDocumentInformation().getCreationDate();
        Calendar modification = document.getDocumentInformation().getModificationDate();
       // document.getDocumentInformation().getCreator()
        //log.debug("document name: " + fileName);
        //log.debug("document creation: " + creation);
        //log.debug("document modification: " + modification);

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
