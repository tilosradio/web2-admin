package hu.tilos.radio.backend.converters;


import hu.radio.tilos.model.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class TagUtilTest {

    @Test
    public void getTags() {
        //given
        String text = "ahoj #bob\n poplacsek #juk asd";
        TagUtil util = new TagUtil();

        //when
        Set<Tag> tags = util.getTags(text);

        //then
        Assert.assertEquals(2,tags.size());

    }

    @Test
    public void replaceToHtml() {
        //given
        String source = "Valami #valami hej ho";

        //when
        String result = new TagUtil().replaceToHtml(source);

        //then
        Assert.assertEquals("Valami <a href=\"/tag/valami\"><span class=\"label label-primary\">valami</span></a> hej ho", result);

    }

}