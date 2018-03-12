package com.extract.processor.parse;

import java.io.InputStream;
import java.io.OutputStream;

public interface Converter {
    void convert(InputStream is, OutputStream os) throws Exception;
}
