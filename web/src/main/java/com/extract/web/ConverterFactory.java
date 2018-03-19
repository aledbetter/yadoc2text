package com.extract.web;

import com.extract.processor.parse.Converter;
import com.extract.processor.parse.Html2Html;
import com.extract.processor.parse.Pdf2Html;
import com.extract.processor.parse.Word2Html;

public class ConverterFactory {
    public static Converter getConverterByFileName(String fileName) {
        switch (getFileExtension(fileName).toLowerCase()) {
            case "pdf": {
                Pdf2Html pdf2Html = new Pdf2Html();
                pdf2Html.setFileName(fileName);
                return pdf2Html;
            }
            case "doc":
            case "docx": {
                Word2Html word2Html = new Word2Html();
                word2Html.setFileName(fileName);
                return word2Html;
            }
            case "htm":
            case "html": {
                Html2Html html2Html = new Html2Html();
                html2Html.setFileName(fileName);
                return html2Html;
            }
            default: {
                return null;
            }
        }
    }
	public static String getFileExtension(String fileName){
		return fileName.substring(
				fileName.lastIndexOf('.') + 1);
	}
}
