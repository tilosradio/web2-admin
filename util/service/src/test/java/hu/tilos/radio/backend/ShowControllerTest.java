package hu.tilos.radio.backend;

import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.types.ShowDetailed;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@RunWith(CdiRunner.class)
@AdditionalClasses(MappingFactory.class)
public class ShowControllerTest {

    @Inject
    ShowController controller;

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void init() {
        emf = TestUtil.initPersistence();
        TestUtil.inidTestData();
    }

    @Test
    public void testGet() throws Exception {
        //given

        controller.setEntityManager(emf.createEntityManager());

        //when
        ShowDetailed show = controller.get("3utas");

        //then
        Assert.assertEquals("3utas", show.getAlias());
        Assert.assertEquals("3. utas", show.getName());

        Assert.assertEquals(2, show.getMixes().size());
        Assert.assertEquals("http://archive.tilos.hu/asd.mp3", show.getMixes().get(0).getFile());

        Assert.assertEquals(2, show.getContributors().size());

        Assert.assertEquals("AUTHOR1", show.getContributors().get(0).getAuthor().getName());
        Assert.assertEquals("http://tilos.hu/upload/avatar/asd.jpg", show.getContributors().get(0).getAuthor().getAvatar());

        Assert.assertEquals(1, show.getSchedulings().size());

        Assert.assertEquals("minden szombat 8:00-10:00",show.getSchedulings().get(0).getText());
    }
}