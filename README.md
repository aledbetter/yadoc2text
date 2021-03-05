
Yet Another Document 2 Text


Extract Text or simplified HTML

	This utility is extracts text or text and some structural information from documents 
	so that the information can be processed. The general use case is for NLP / NLU where the
	document structural information are needed to add semantic context to the content. 
	The text output is the same, just without the html tags.
	OCR is not supported, this project does not (currently) work with images


build and run locally

	1. go to base directory of branch, this will build the package with everything in it
	2. prompt# brew install maven
    3. mvn clean
    4. mvn install
    5. cd web
    6. mvn jetty:run
       mvn jetty:run -Djetty.port=8099
       old: mvn jetty:run -Dhttp.port=8099
       old: mvn jetty:run -Djetty.http.port=8099   
    7. index is test page

	Broken Build -in eclipse circular dependency in project
	1. comment out code references to org.apache.pdfbox.* in ParsePdf.java
		- imports and body of parseData() method
	2. maven install
	3. run in jetty
	4. uncomment code
	5. maven install again
    
Supported Document Types

	Word: .doc, .docx, .dot
	PDF: .pdf
	html: .html, .htm, .mht
	text: .text, .txt
	richtext: .rtf
    
Converted file html tags

	Title: <title>
	Headings: <h1>, <h2>, <h3>...<hn>
	Text: <b>, <u>, <i>
	Structure: <p>, <header>, <footer>
	Lists: <ol>, <ul>, <li>
	Sections: <section>, <article>  TODO
	Tables: TBD
	
Converted meta info

	Document Type: 
		<meta name="doc-type" content="html">
	Original Document Name: 
		<meta name="doc-name" content="test.html">
	Created Time
		<meta name="doc-created" content="xxxxx">
	Modified Time
		<meta name="doc-modified" content="xxx">
	Author
		<meta name="doc-author" content="Bober Simthsonsons">
	Language
		<meta name="doc-language" content="en">	
	Url
		<meta name="doc-url" content="http://www.sample.com/moby-dick.html">	
		
		
		
		
		
	
	
	
