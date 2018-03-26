Bugs, changes and enhancements


DOCs (.doc, .docx)

	1) Missing titles / content missing
	2) Bold content from tables not bold

PDF (.pdf)

	1) all text has spaces striped
	2) ul not identified each set to start with: <p>§
	3) Mar 13, 2018 4:06:58 PM org.apache.pdfbox.cos.COSDocument finalize
	   WARNING: Warning: You did not close a PDF Document


HTML (.htm, .html)

    1) retain <section> and <article> tags in location. don't retain any attributes

 
TEXT (.txt)

	1) support .txt files and produce simplified html
	2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
	3) <p> where lines are ended early (and not <li>
	   or when large gap between words see example
   
   