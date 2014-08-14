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

public class PersistentEpisodeProviderTest {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DataSource ds;
    @BeforeClass
    public static void setUp() {
        ds = TestUtil.initDatasource();
        TestUtil.inidTestData();
    }

    @Test
    public void testListEpisode() throws Exception {
        //given
        PersistentEpisodeProvider p = new PersistentEpisodeProvider(TestUtil.initDatasource());


        //when
        List<EpisodeData> episodes = p.listEpisode(1, SDF.parse("2014-04-03 12:00:00"), SDF.parse("2014-05-03 12:00:00"));

        //then
        Assert.assertEquals(2, episodes.size());
        Assert.assertNotNull(episodes.get(0).getShow());
        Assert.assertNotNull(episodes.get(1).getText());


    }
}