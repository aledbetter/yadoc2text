package com.extract.web;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FileExtensionTest {

    @Test
    public void test() {
        Map<String, String> fileNamesAndExtensions = new HashMap<>();
        fileNamesAndExtensions.put("testFileName.pdf", "pdf");
        fileNamesAndExtensions.put("testFileName.docx", "docx");
        fileNamesAndExtensions.put("test.file.name.pdf", "pdf");
        fileNamesAndExtensions.put("test.file.name.docx", "docx");

        for (String fileName : fileNamesAndExtensions.keySet()) {
            Assert.assertEquals(
                    fileNamesAndExtensions.get(fileName),
                    Gtil.getFileExtension(fileName)
            );
        }
    }

}
