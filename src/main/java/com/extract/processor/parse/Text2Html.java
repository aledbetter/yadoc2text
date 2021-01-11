package main.java.com.extract.processor.parse;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Text2Html implements Converter2Html {

    private static final String TYPE = "text";

    private String fileName;

    public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
    public void convert(InputStream is, OutputStream os) throws IOException {

        String in = readStream(is);

        os.write(in.getBytes());
    }


}
