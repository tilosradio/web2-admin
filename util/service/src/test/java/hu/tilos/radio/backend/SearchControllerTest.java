package hu.tilos.radio.backend;

import hu.tilos.radio.backend.data.SearchResponse;
import hu.tilos.radio.backend.data.SearchResponseElement;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.openjpa.persistence.jdbc.Index;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.Persistence;
import java.io.IOException;

import static org.junit.Assert.*;

public class SearchControllerTest {

    @Test
    @Ignore
    public void test() throws IOException, ParseException {
        //given
        SearchController controller = new SearchController();
        controller.setEntityManagerFactory(Persistence.createEntityManagerFactory("tilos-test"));

        //when
        SearchResponse respo = controller.search("tamogatas");

        //then
        Assert.assertEquals(1, respo.getElements().size());
        for (SearchResponseElement element : respo.getElements()) {
            System.out.println(element.getAlias());
            System.out.println(element.getTitle());
        }
    }

}