package com.extract.processor;

import com.extract.processor.parse.Html2Html;
import com.extract.processor.utils.HtmlUtils;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Log4j2
public class HtmlTest {

    @Test
    public void headerLevel() {
        Assert.assertEquals(1, HtmlUtils.getHeaderLevel("h1"));
        Assert.assertEquals(2, HtmlUtils.getHeaderLevel("h2"));
        Assert.assertEquals(3, HtmlUtils.getHeaderLevel("h3"));
        Assert.assertEquals(4, HtmlUtils.getHeaderLevel("h4"));
        Assert.assertEquals(5, HtmlUtils.getHeaderLevel("h5"));
        Assert.assertEquals(6, HtmlUtils.getHeaderLevel("h6"));
    }

    @Test
    public void styled() throws Exception {
        Document document = Jsoup.parse(
                this.getClass().getResourceAsStream("/is_styled_test_data.html"),
                "UTF-8", "");
        Set<String> boldSelectors = new HashSet<>();
        boldSelectors.add("body > p:nth-child(2) > b:nth-child(5) > i:nth-child(1) > u:nth-child(1)");
        boldSelectors.add("body > p:nth-child(2) > b:nth-child(4) > i:nth-child(1)");
        boldSelectors.add("body > p:nth-child(4) > b:nth-child(1)");
        Set<String> italicSelectors = new HashSet<>();
        italicSelectors.add("body > p:nth-child(2) > b:nth-child(5) > i:nth-child(1) > u:nth-child(1)");
        italicSelectors.add("body > ul:nth-child(9) > li:nth-child(2) > i:nth-child(1)");
        Set<String> underlinedSelectors = new HashSet<>();
        underlinedSelectors.add("body > p:nth-child(4) > b:nth-child(5) > i:nth-child(1) > u:nth-child(1)");
        underlinedSelectors.add("body > ul:nth-child(9) > li:nth-child(1) > u:nth-child(1)");
        underlinedSelectors.add("body > p:nth-child(8) > u:nth-child(3)");
        for (String boldSelector : boldSelectors) {
            boolean result = HtmlUtils.isBold(document.selectFirst(boldSelector));
            if (!result) {
                log.debug("bold not determined: " + boldSelector);
            }
            Assert.assertTrue(result);
        }
        for (String italicSelector : italicSelectors) {
            boolean result = HtmlUtils.isItalic(document.selectFirst(italicSelector));
            if (!result) {
                log.debug("italic not determined: " + italicSelector);
            }
            Assert.assertTrue(result);
        }
        for (String underlinedSelector : underlinedSelectors) {
            boolean result = HtmlUtils.isUnderlined(document.selectFirst(underlinedSelector));
            if (!result) {
                log.debug("underlined not determined: " + underlinedSelector);
            }
            Assert.assertTrue(result);
        }
    }

    @Test
    public void sample() throws IOException {
        convert("test_data/sample1.html", "result_html1.html");
    }

    @Test
    public void sample2() throws IOException {
        convert("test_data/sample2.html", "result_html2.html");
    }

    @Test
    public void sample3() throws IOException {
        convert("test_data/sample3.html", "result_html3.html");
    }

    @Test
    public void sample4() throws IOException {
        convert("test_data/sample4.html", "result_html4.html");
    }

    @Test
    public void sample5() throws IOException {
        convert("test_data/sample5.html", "result_html5.html");
    }

    private void convert(String sourcePath, String resultPath) throws IOException {
        try (InputStream is = new FileInputStream(sourcePath);
             OutputStream os = new FileOutputStream(resultPath)) {
            Html2Html html2Html = new Html2Html();
            html2Html.convert(is, os);
        }
    }
}
