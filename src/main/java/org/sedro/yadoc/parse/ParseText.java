package org.sedro.yadoc.parse;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.sedro.yadoc.model.MDocument;
import org.sedro.yadoc.model.MElement;
import org.sedro.yadoc.render.DocumentRender;


public class ParseText implements YaParser {

    private static final String TYPE = "text";
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
        /*
        MParagraph paragraph = new MParagraph();
		String in = readStream(is);
		// bad hack
		in = in.replace('�', '∙');
		
		// add block of text as paragraph
	// FIXME this may not be good..
        List<MText> result = new ArrayList<>();
        MText text = new MText();
        text.setText(in);
        result.add(text);
        paragraph.setTexts(result);
        simpleHtml.getElementList().add(paragraph);
        */
        return simpleHtml;       
	}
	
	@Override
    public void convert(MDocument data, InputStream is, OutputStream os) throws Exception {
		String in = readStream(is);
		// bad hack
		in = in.replace('�', '∙');
		
        //System.out.println(in);
        StringBuilder result = DocumentRender.renderHdr(data);
        result.append(in);

        result = DocumentRender.renderFtr(data, result);
 // FIXME CHARSET?    
        os.write(result.toString().getBytes());
      //  System.out.println(result.toString());
    }
	@Override
   public void convertText(MDocument data, InputStream is, OutputStream os) throws Exception {
		String in = readStream(is);
		// bad hack
		in = in.replace('�', '∙');		
        //System.out.println(in);       
        os.write(in.getBytes());
    }
 	
	public String readStream(InputStream input) {	
	    int BUFFER_SIZE = 8192;	
	    StringBuilder sb = new StringBuilder();
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"), BUFFER_SIZE);
		    String str;
		    while ((str = br.readLine()) != null) {
		    	sb.append(str);
		    	sb.append('\n');
		    }
		} catch (Exception e) {

		}
		return sb.toString();
	}


}
