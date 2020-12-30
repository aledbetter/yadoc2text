package main.java.com.extract.web;

import main.java.com.extract.processor.parse.ConverterHtml;
import main.java.com.extract.processor.parse.Html2Html;
import main.java.com.extract.processor.parse.Pdf2Html;
import main.java.com.extract.processor.parse.Word2Html;

public class ConverterFactory {
    public static ConverterHtml getConverterByFileName(String fileName) {
    	
    	//.DOC, .RTF, .DOCX, .ODT, .DOT, .PDF, .HTM, .HTML, .MHT file formats.
        switch (getFileExtension(fileName).toLowerCase()) {
            case "pdf": {
                Pdf2Html pdf2Html = new Pdf2Html();
                pdf2Html.setFileName(fileName);
                return pdf2Html;
            }
            case "dot":
            case "doc":
            case "docx": {
                Word2Html word2Html = new Word2Html();
                word2Html.setFileName(fileName);
                return word2Html;
            }
            case "mht":
            case "htm":
            case "html": {
                Html2Html html2Html = new Html2Html();
                html2Html.setFileName(fileName);
                return html2Html;
            }
            case "rtf": {
                //FIXME
                return null;
            }  
            case "odt": {
                //FIXME
                return null;
            } 
            case "text":
            case "txt": {
                //FIXME
                return null;
            }            
            default: {
                return null;
            }
        }
    }
	public static String getFileExtension(String fileName){
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}
}
