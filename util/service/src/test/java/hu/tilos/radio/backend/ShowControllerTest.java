package hu.tilos.radio.backend;

import hu.tilos.radio.backend.data.types.ShowDetailed;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;

public class ShowControllerTest {

    private static DataSource dataSource;

    @BeforeClass
    public static void init() {
        dataSource = TestUtil.initDatasource();
        TestUtil.inidTestData();
    }

    @Test
    public void testGet() throws Exception {
        //given

        ShowController controller = new ShowController();
        controller.setDatasource(dataSource);

        //when
        ShowDetailed show = controller.get("3utas");

        //then
        Assert.assertEquals("3utas", show.getAlias());
        Assert.assertEquals("3. utas", show.getName());

        Assert.assertEquals(2, show.getMixes().size());
        Assert.assertEquals("asd.mp3", show.getMixes().get(0).getFile());

       // Assert.assertEquals(2, show.getContributors().size());
    }
}