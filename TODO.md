

DOCs (.doc, .docx)
1) mpty tags (tabs or other whitespace inside)
	<p>
		<b>aaron.</b><b>ledbetter</b><b>.1</b><b>@</b><b>gmail</b><b>.com</b><b>
		</b><b> </b><b> </b><b> </b><b> </b><b> </b><b> </b><b> </b><b>Los
			Altos, Ca</b>
	</p>
2) Bold content from tables not bolded
3) Missing titles / content missing


PDF (.pdf)
1) all text has spaces striped
2) ul not identified each set to start with: <p>§
3) Mar 13, 2018 4:06:58 PM org.apache.pdfbox.cos.COSDocument finalize
   WARNING: Warning: You did not close a PDF Document


HTML (.htm, .html)
1) <nav> content in under nav should just be removed (configurable)
2) meta lang tag from page indicated language 
3) <p></p> still found in docs (https://en.wikipedia.org/wiki/Precession)
4) html annotations should be ignored if not in encoding != html/text
       <annotation encoding="application/x-tex">{\displaystyle {\boldsymbol {\omega }}_{\mathrm {p} }={\frac {{\boldsymbol {I}}_{\mathrm {s} }{\boldsymbol 	{\omega }}_{\mathrm {s} }}{{\boldsymbol {I}}_{\mathrm {p} }\cos({\boldsymbol {\alpha }})}}}</annotation>
5) don't include any items or their children if taged with: role=[navigation, menu, menubar, menuitem]
6) if attribute: id=footer OR role=footer then consider the item and children to be in footer
7) list of string to remove / not put in final:
   // "[edit]"
8) much missing content (test2.html)
9) issue with very small html files (under 2k) java.lang.IllegalStateException: Stream already closed
	<html><head><title>3rd nor'easter in 2 weeks to hit East Coast - ABC News</title><meta name="doc-type" content="html"/><meta name="doc-name" content="story?id=53678598.html"/></head><body>this is just plain text so nothing raw should come around or around</body></html>
	
  
  
   
TEXT (.txt)
1) support .txt files and produce simplified html
2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
3) <p> where lines are ended early (and not <li>
   or when large gap between words see example
   
   