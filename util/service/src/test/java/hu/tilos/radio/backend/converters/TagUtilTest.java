package hu.tilos.radio.backend.converters;


import org.junit.Assert;
import org.junit.Test;

public class TagUtilTest {

    @Test
    public void testReplaceToHtml() {
        //given
        String source = "Valami #valami hej ho";

        //when
        String result = new TagUtil().replaceToHtml(source);

        //then
        Assert.assertEquals("Valami <a href=\"/tag/valami\"><span class=\"label label-primary\">valami</span></a> hej ho", result);

    }

}