package main.java.com.extract;

import main.java.com.extract.processor.parse.Converter2Html;
import main.java.com.extract.processor.parse.Pdf2Html;
import main.java.com.extract.processor.parse.Word2Html;
import main.java.com.extract.rest.ConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.ws.rs.core.Response;


/*
 * Command Line
 * Run stand alone
 * DocExtract <text/html> <filename>
 */
public class DocExtract {
    public static void main(String[] args) throws Exception {
    	
        if (args.length != 2 || (args.length > 1 && args[1].startsWith("-"))) {
            System.err.println("usage: <text/html> <filename>");
            System.exit(1);
        }
        
        String infile = args[1].toLowerCase();
        
        // get converter
        Converter2Html converter = ConverterFactory.getConverterByFileName(infile);
        if (converter == null){
        	System.err.println("File type not supported: " + infile);
        	System.exit(2);          
        }       
        try {
	        if (args[0].equalsIgnoreCase("text")) {
	        	// to text
	            String outfile = infile+".txt";           
	            converter.convertDataText(new FileInputStream(infile), new FileOutputStream(outfile));
	        } else {
	        	// to html
	            String outfile = infile+".html";
	            converter.convertDataHtml(new FileInputStream(infile), new FileOutputStream(outfile));
	        }
        } catch (Exception e) {
            System.err.println("DocExtract "+args[0]+" "+infile+" ERROR: " + e.getMessage());
            e.printStackTrace();
        	System.exit(2);          
        }   
        
    }
}
