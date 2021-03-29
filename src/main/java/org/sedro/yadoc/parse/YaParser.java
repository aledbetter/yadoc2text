package org.sedro.yadoc.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.sedro.yadoc.model.MDocument;
import org.sedro.yadoc.render.DocumentRender;

public interface YaParser {
    public MDocument parseData(InputStream is, OutputStream os) throws Exception;


    public default void convert(MDocument data, InputStream is, OutputStream os) throws Exception {
        os.write(DocumentRender.render(data).getBytes());
    }
    public default void convertText(MDocument data, InputStream is, OutputStream os) throws Exception {
        os.write(DocumentRender.renderText(data).getBytes());
    }
    
    public default void convertDataHtml(InputStream is, OutputStream os) throws Exception {
    	MDocument data = parseData(is, os);
        convert(data, is, os);
    }
    public default void convertDataText(InputStream is, OutputStream os) throws Exception {
    	MDocument data = parseData(is, os);
    	convertText(data, is, os);
    }
    
	public default String readStream(InputStream input) {	
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
		try {
			return bOutput.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		return bOutput.toString();
	}
}
