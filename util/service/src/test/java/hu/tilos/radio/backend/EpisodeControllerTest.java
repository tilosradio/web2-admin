package hu.tilos.radio.backend;

import hu.radio.tilos.model.Episode;
import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.CreateResponse;
import hu.tilos.radio.backend.data.UpdateResponse;
import hu.tilos.radio.backend.data.types.EpisodeData;
import hu.tilos.radio.backend.data.types.ShowSimple;
import hu.tilos.radio.backend.data.types.TextData;
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
        EpisodeData episode = controller.getByDate("3utas", 2014, 04, 11);

        //then
        Assert.assertNotNull(episode.getText());
        Assert.assertEquals("Jo musor", episode.getText().getTitle());
    }

    @Test
    public void testCreateEpisode() throws Exception {
        //given
        EpisodeData episode = new EpisodeData();
        episode.setPlannedFrom(TestUtil.YYYYMMDDHHMM.parse("201405011200").getTime());
        episode.setPlannedTo(TestUtil.YYYYMMDDHHMM.parse("201405011300").getTime());

        ShowSimple simple = new ShowSimple();
        simple.setId(1);
        episode.setShow(simple);

        TextData td = new TextData();
        td.setTitle("Title");
        td.setContent("ahoj");
        episode.setText(td);

        //when
        controller.getEntityManager().getTransaction().begin();
        CreateResponse createResponse = controller.create(episode);
        controller.getEntityManager().getTransaction().commit();

        //then
        Episode episodeEntity = controller.getEntityManager().find(Episode.class, createResponse.getId());
        Assert.assertNotNull(episodeEntity.getText());
        Assert.assertEquals("Title", episodeEntity.getText().getTitle());
        Assert.assertEquals(1, episodeEntity.getShow().getId());
    }

    @Test
    public void testUpdateEpisode() throws Exception {
        //given
        EpisodeData episode = controller.get(2);
        episode.setPlannedFrom(TestUtil.YYYYMMDDHHMM.parse("201405011200").getTime());
        episode.setPlannedTo(TestUtil.YYYYMMDDHHMM.parse("201405011300").getTime());

        episode.getText().setContent("ez jobb");

        //when
        controller.getEntityManager().getTransaction().begin();
        UpdateResponse createResponse = controller.update(2, episode);
        controller.getEntityManager().getTransaction().commit();

        //then
        Episode episodeEntity = controller.getEntityManager().find(Episode.class, createResponse.getId());
        Assert.assertNotNull(episodeEntity.getText());
        Assert.assertEquals("ez jobb", episodeEntity.getText().getContent());
    }
}