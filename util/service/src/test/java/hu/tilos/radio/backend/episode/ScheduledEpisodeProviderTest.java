package hu.tilos.radio.backend.episode;

import hu.tilos.radio.backend.TestUtil;
import hu.tilos.radio.backend.data.EpisodeData;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduledEpisodeProviderTest {

    private static DataSource ds;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeClass
    public static void setUp() {
        ds = TestUtil.initDatasource();
        TestUtil.inidTestData();
    }

    @Test
    public void testListEpisode() throws Exception {
        //given
        ScheduledEpisodeProvider p = new ScheduledEpisodeProvider(ds);

        //when
        List<EpisodeData> episodes = p.listEpisode(1, SDF.parse("2014-04-03 12:00:00"), SDF.parse("2014-05-03 12:00:00"));

        //then
        Assert.assertEquals(3, episodes.size());

    }

    @Test
    public void testListEpisodeWithBase() throws Exception {
        //given
        ScheduledEpisodeProvider p = new ScheduledEpisodeProvider(ds);

        //when
        List<EpisodeData> episodes = p.listEpisode(3, SDF.parse("2014-04-03 12:00:00"), SDF.parse("2014-05-03 12:00:00"));

        //then
        Assert.assertEquals(2, episodes.size());
    }
}