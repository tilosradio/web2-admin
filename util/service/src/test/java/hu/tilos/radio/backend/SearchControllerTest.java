package hu.tilos.radio.backend;

import hu.tilos.radio.backend.data.SearchResponse;
import hu.tilos.radio.backend.data.SearchResponseElement;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.openjpa.persistence.jdbc.Index;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.Persistence;
import java.io.IOException;

import static org.junit.Assert.*;

public class SearchControllerTest {

    @BeforeClass
    public static void testDataInit() throws Exception {
        TestUtil.inidTestData();
    }

    @Test
    public void test() throws IOException, ParseException {
        //given
        SearchController controller = new SearchController(TestUtil.initDatasource());

        //when
        SearchResponse respo = controller.search("tamogatas");

        //then
        Assert.assertEquals(1, respo.getElements().size());
        SearchResponseElement searchResponseElement = respo.getElements().get(0);
        Assert.assertEquals("asd", searchResponseElement.getAlias());
    }

}