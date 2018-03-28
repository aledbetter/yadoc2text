Bugs, changes and enhancements


DOCs (.doc, .docx)


PDF (.pdf)

	1) extract headers and establish header level by useing the font sizes found in the document (like in docs)

HTML (.htm, .html)

    1) retain <section> and <article> tags in location. don't retain any attributes
    2) <p> merged without a space; should be 
      <p>Analysis by Chris Cillizza, CNN Editor-at-large</p>
      <p>Updated 6:12 PM ET, Fri March 23, 2018</p>
      
    HTML
	<div class="metadata__info js-byline-images" data-bundle="byline">
		<p class="metadata__byline">
			<span class="metadata__byline__author">
			Analysis by <a href="/profiles/chris-cillizza">Chris Cillizza</a>, CNN Editor-at-large </span>
		</p>
		<p class="metadata__show"></p>
		<p class="update-time">
			Updated 6:12 PM ET, Fri March 23, 2018 <span id="js-pagetop_video_source" class="video__source top_source"></span>
		</p>
	</div>


 
TEXT (.txt)

	1) support .txt files and produce simplified html
	2) <ul><ol> supported, lines that start with number/special char and have multiple such lines in succession
	3) <p> where lines are ended early (and not <li>
	   or when large gap between words see example
   
   