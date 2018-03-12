package com.extract.processor;

import com.extract.processor.parse.Pdf2Html;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfTest {

    @Test
    public void sample() throws Exception {
        convert("test_data/sample.pdf", "result_pdf.html");
    }

    @Test
    public void sample2() throws Exception {
        convert("test_data/sample2.pdf", "result_pdf2.html");
    }

    @Test
    public void sample3() throws Exception {
        convert("test_data/sample3.pdf", "result_pdf3.html");
    }

    @Test
    public void sample4() throws Exception {
        convert("test_data/sample4.pdf", "result_pdf4.html");
    }

    @Test
    public void sample5() throws Exception {
        convert("test_data/sample5.pdf", "result_pdf5.html");
    }

    @Test
    public void sample6() throws Exception {
        convert("test_data/sample6.pdf", "result_pdf6.html");
    }

    private void convert(String sourcePath, String resultPath) throws Exception {
        try (InputStream is = new FileInputStream(sourcePath);
             OutputStream os = new FileOutputStream(resultPath)) {
            new Pdf2Html().convert(is, os);
        }
    }
}
