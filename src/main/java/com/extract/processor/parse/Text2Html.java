package main.java.com.extract.processor.parse;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
