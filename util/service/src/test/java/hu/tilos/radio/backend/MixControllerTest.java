package hu.tilos.radio.backend;

import hu.radio.tilos.model.Mix;
import hu.tilos.radio.backend.data.CreateResponse;
import hu.tilos.radio.backend.data.EntitySelector;
import hu.tilos.radio.backend.data.MixRequest;
import hu.tilos.radio.backend.data.MixResponse;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dozer.CustomFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.dozer.loader.api.BeanMappingBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Properties;

public class MixControllerTest {
    private static EntityManagerFactory factory;

    @BeforeClass
    public static void setUp() throws Exception {
        factory = TestUtil.initPersistence();
        TestUtil.inidTestData();
    }

    @Test
    public void testGet() {

        //given
        MixController controller = new MixController();
        controller.setEntityManager(factory.createEntityManager());

        //when
        MixResponse r = controller.get(1);

        //then
        Assert.assertEquals("good mix", r.getTitle());
        Assert.assertNotNull(r.getShow());
        Assert.assertEquals(1, r.getType());
        Assert.assertEquals("Zene", r.getTypeText());
        Assert.assertEquals("3. utas", r.getShow().getName());
    }


    @Test
    public void testList() {

        //given
        MixController controller = new MixController();
        controller.setEntityManager(factory.createEntityManager());

        //when
        List<MixResponse> responses = controller.list();

        //then
        Assert.assertEquals(2, responses.size());
    }


    @Test
    public void testCreate() {

        //given
        MixController controller = new MixController();
        EntityManager em = factory.createEntityManager();

        controller.setEntityManager(em);

        MixRequest r = new MixRequest();
        r.setAuthor("lajos");
        r.setTitle("new mix");
        r.setFile("lajos.mp3");

        //when
        em.getTransaction().begin();
        CreateResponse response = controller.create(r);
        em.getTransaction().commit();

        //then
        Assert.assertTrue(response.isSuccess());
        Assert.assertNotEquals(0, response.getId());

        Mix mix = em.find(Mix.class, response.getId());
        Assert.assertEquals("lajos", mix.getAuthor());
        em.close();
    }


    @Test
    public void testUpdate() {

        //given
        MixController controller = new MixController();
        EntityManager em = factory.createEntityManager();
        controller.setEntityManager(em);
        MixResponse r = controller.get(1);

        MixRequest req = new MixRequest();
        req.setAuthor(r.getAuthor());
        req.setFile(r.getFile());
        req.setId(r.getId());
        req.setTitle("this Is the title");
        req.setDate("2014-10-23");
        req.setShow(new EntitySelector(2));

        //when
        em.getTransaction().begin();
        CreateResponse response = controller.update(1, req);
        em.getTransaction().commit();

        //then
        Assert.assertTrue(response.isSuccess());


        Mix mix = em.find(Mix.class, 1);
        Assert.assertEquals("this Is the title", mix.getTitle());
        Assert.assertEquals(9, mix.getDate().getMonth());
        Assert.assertEquals(23, mix.getDate().getDate());
        em.close();
    }


}