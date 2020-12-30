package main.java.com.extract.processor.parse;

import java.io.InputStream;
import java.io.OutputStream;

public interface ConverterHtml {
    void convert(InputStream is, OutputStream os) throws Exception;
}
