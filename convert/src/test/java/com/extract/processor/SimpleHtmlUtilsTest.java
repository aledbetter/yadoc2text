package com.extract.processor;

import com.extract.processor.model.Text;
import com.extract.processor.utils.SimpleHtmlUtils;
import org.junit.Assert;
import org.junit.Test;

public class SimpleHtmlUtilsTest {

    @Test
    public void checkSimilarFontParams() {
        Text text1 = new Text();
        text1.setItalic(true);
        text1.setBold(false);
        Text text2 = new Text();
        text2.setItalic(true);
        text2.setBold(false);
        Assert.assertTrue(SimpleHtmlUtils.checkSimilarFontParams(text1, text2));
        text2.setBold(true);
        Assert.assertFalse(SimpleHtmlUtils.checkSimilarFontParams(text1, text2));
        text1.setBold(true);
        Assert.assertTrue(SimpleHtmlUtils.checkSimilarFontParams(text1, text2));
    }

}
