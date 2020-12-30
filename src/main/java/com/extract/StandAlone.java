package main.java.com.extract;

import main.java.com.extract.processor.parse.ConverterHtml;
import main.java.com.extract.processor.parse.Pdf2Html;
import main.java.com.extract.processor.parse.Word2Html;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class StandAlone {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("use 3 arguments: sourcePath, resultPath, type[pdf,word]");
            System.exit(1);
        }
        ConverterHtml converter = null;
        switch (args[2].toLowerCase()) {
            case "pdf": {
                converter = new Pdf2Html();
            }
            break;
            case "word": {
                converter = new Word2Html();
            }
            break;
            default: {
                System.err.println("third argument can contains only 'pdf' or 'word'");
                System.exit(2);
            }
        }
        converter.convert(new FileInputStream(args[0]), new FileOutputStream(args[1]));
    }
}
