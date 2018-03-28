Bugs, changes and enhancements


DOCs (.doc, .docx)


PDF (.pdf)

	1) extract headers and establish header level by using the font sizes found in the document (like in docs)

HTML (.htm, .html)

    1) retain <section> and <article> tags in location. don't retain any attributes
    2) br => add space, p OR '.' 
    3) multiple div/ in li, must render each div/p with a p
    4) (CNN)About => should be different paragrpah for <cite>
 
TEXT (.txt)

	1) support .txt files and produce simplified html
	2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
	3) <p> where lines are ended early (and not <li>
	   or when large gap between words see example
   
PPT (.ppt)
	
	1) support powerpoint conversion. each page in <section>