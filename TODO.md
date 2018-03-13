

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
9) issue with very small html files (under 2k)
	 CONVERT FILE: input.html SIZE: -1
	CONVERT FILE ERROR:
	java.lang.IllegalStateException: Stream already closed
		at org.jvnet.mimepull.DataHead$ReadMultiStream.fetch(DataHead.java:237)
		at org.jvnet.mimepull.DataHead$ReadMultiStream.read(DataHead.java:212)
		at java.io.BufferedInputStream.fill(BufferedInputStream.java:246)
		at java.io.BufferedInputStream.read(BufferedInputStream.java:265)
		at org.jsoup.helper.DataUtil.parseInputStream(DataUtil.java:104)
		at org.jsoup.helper.DataUtil.load(DataUtil.java:63)
		at org.jsoup.Jsoup.parse(Jsoup.java:118)
		at com.extract.processor.parse.Html2Html.convert(Html2Html.java:38)
		at com.extract.web.RestAPI.uploadDocumentPost(RestAPI.java:53)
		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) 
  
  
   
TEXT (.txt)
1) support .txt files and produce simplified html
2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
3) <p> where lines are ended early (and not <li>
   or when large gap between words see example
   
   