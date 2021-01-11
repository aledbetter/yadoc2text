package main.java.com.extract.processor.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public interface Converter2Html {
    void convert(InputStream is, OutputStream os) throws Exception;
    
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
