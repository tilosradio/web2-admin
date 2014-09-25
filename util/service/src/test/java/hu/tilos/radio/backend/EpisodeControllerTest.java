package hu.tilos.radio.backend;

import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.types.EpisodeData;
import org.junit.Assert;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.*;

@RunWith(CdiRunner.class)
@AdditionalClasses({MappingFactory.class, TestUtil.class})
public class EpisodeControllerTest {

    @Inject
    EpisodeController controller;

    @BeforeClass
    public static void setUp() throws Exception {
        EntityManagerFactory factory = TestUtil.initPersistence();
        TestUtil.initTestData();
    }

    @Test
    public void testGet() throws Exception {
        //given

        //when
        EpisodeData episode = controller.get(2);

        //then
        Assert.assertNotNull(episode.getText());
        Assert.assertEquals("Jo musor", episode.getText().getTitle());
    }


    @Test
    public void testGetByDate() throws Exception {
        //given

        //when
        EpisodeData episode = controller.getByDate("3utas",2014,04,11);

        //then
        Assert.assertNotNull(episode.getText());
        Assert.assertEquals("Jo musor", episode.getText().getTitle());
    }
}