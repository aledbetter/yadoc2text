package com.extract.processor;

import com.extract.processor.parse.Word2Html;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import java.io.*;

public class WordTest {

    @Test
    public void sample() throws Exception {
        convert("test_data/sample.docx", "result_word.html");
    }

    @Test
    public void sample2() throws Exception {
        convert("test_data/sample2.docx", "result_word2.html");
    }

    @Test
    public void sample3() throws Exception {
        convert("test_data/sample3.docx", "result_word3.html");
    }

    @Test
    public void sample4() throws Exception {
        convert("test_data/sample4.docx", "result_word4.html");
    }

    @Test
    public void sample5() throws Exception {
        convert("test_data/sample5.docx", "result_word5.html");
    }

    private void convert(String sourcePath, String resultPath) throws IOException,
            OpenXML4JException, XmlException {
        try (InputStream is = new FileInputStream(sourcePath);
             OutputStream os = new FileOutputStream(resultPath)) {
            new Word2Html().convert(is, os);
        }
    }
}
