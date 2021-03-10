package org.sedro.yadoc.parse;

public class YaParserFactory {
    public static YaParser getConverterByFileName(String fileName) {
    	
    	//.DOC, .RTF, .DOCX, .ODT, .DOT, .PDF, .HTM, .HTML, .MHT file formats.
        switch (getFileExtension(fileName).toLowerCase()) {
            case "pdf": {
                ParsePdf pdf2Html = new ParsePdf();
                pdf2Html.setFileName(fileName);
                return pdf2Html;
            }
            case "dot":
            case "doc":
            case "docx": {
                ParseWord word2Html = new ParseWord();
                word2Html.setFileName(fileName);
                return word2Html;
            }
            case "mht":
            case "htm":
            case "html": {
                ParseHtml html2Html = new ParseHtml();
                html2Html.setFileName(fileName);
                return html2Html;
            }
            case "rtf": {
            	ParseRtf rtf2Html = new ParseRtf();
            	rtf2Html.setFileName(fileName);
                return rtf2Html;
            }  
            case "odt": {
                //FIXME
                return null;
            } 
            case "text":
            case "txt": {
                ParseText text2Html = new ParseText();
                text2Html.setFileName(fileName);
                return text2Html;
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
