Bugs, changes and enhancements


DOCs (.doc, .docx)

	1) mpty tags (tabs or other whitespace inside)
		<p>
			<b>aaron.</b><b>ledbetter</b><b>.1</b><b>@</b><b>gmail</b><b>.com</b><b>
			</b><b> </b><b> </b><b> </b><b> </b><b> </b><b> </b><b> </b><b>Los
				Altos, Ca</b>
		</p>
	2) Bold content from tables not bold
	3) Missing titles / content missing
	4) special chars in output after "by Boeing)"


PDF (.pdf)

	1) all text has spaces striped
	2) ul not identified each set to start with: <p>§
	3) Mar 13, 2018 4:06:58 PM org.apache.pdfbox.cos.COSDocument finalize
	   WARNING: Warning: You did not close a PDF Document


HTML (.htm, .html)

	1) much missing content (test2.html). when content is in <div> it is not shown
		<div>McCabe <a href="index.html">defended himself</a> and argued his firing</div>
	2) apostrophe"’" converts to 2 unreadable special chars in output (test6.html)
    3) bad special char conversions: &trade; &amp; &copy;
    4) <ul> with no <li> should be treated as <p>
    5) in <ul> or <li> retain <hX> within or outside of <li>
    6) retain <section> and <article> tags in location. don't retain any attributes
    7) treat string of <span> or <a> as single <p>
       <a href="/terms">Terms of service</a> | <a href="/privacy">Privacy guidelines</a> | <a target="_blank">AdChoices</a>
	8) meta lang tag in results when the language is avaliable (body attribute or meta tag)
	9) don't include any items or their children if attribute with: role=[navigation, menu, menubar, menuitem]
	10) if attribute: id=footer OR role=footer then consider the item and children to be in footer
	11) issue with processing the output html files: java.lang.IllegalStateException: Stream already closed
		<html><head><title>3rd nor'easter in 2 weeks to hit East Coast - ABC News</title><meta name="doc-type" content="html"/><meta name="doc-name" content="story?id=53678598.html"/></head><body>this is just plain text so nothing raw should come around or around</body></html>
 
 
TEXT (.txt)

	1) support .txt files and produce simplified html
	2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
	3) <p> where lines are ended early (and not <li>
	   or when large gap between words see example
   
   