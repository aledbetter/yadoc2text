Bugs, changes and enhancements

build:

	issue with the org.apache.pdfbox.text class in maven build
	- load, build class in eclipse, then build install generates code
	- best fix would be to remove this method and call the PDF stripper directly



DOCs (.doc, .docx)

	1) better testing for .dot and older formats of doc needed
	2) image content not supported


PDF (.pdf)

	1) improve heading identification
	2) image content not supported

HTML (.htm, .html)

    1) retain <section> and <article> tags in location. don't retain any attributes
    2) <figure> decide where to put these or how to tag them
    3) use font-height to determine headers
    4) better tracking for bold/italics; should manage a state across nodes

TEXT (.txt, .text)

	1) support .txt files and produce simplified html
	2) support headings and lists from text
   
RTF (.rtf)
	
	1) generate internal structures directly. Current is rtf -> html -> simplified html/text
	2) properly map lists and headings
	3) identify and remove footer/header where appropriate
	4) image content not supported
	
ODT (.odt)

	1) add support for open office

PPT (.ppt)

	1) add support for powerpoint
	
Excel (.xlsx)

	1) add support for excel
	
