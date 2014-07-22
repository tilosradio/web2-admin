package hu.tilos.radio.backend;

import hu.tilos.radio.backend.episode.EpisodeUtil;
import hu.tilos.radio.backend.episode.PersistentEpisodeProvider;
import hu.tilos.radio.backend.episode.ScheduledEpisodeProvider;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class FeedControllerTest {

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setup() {
        emf = TestUtil.initPersistence();
        TestUtil.inidTestData();

    }

    @Test
    public void testFeed() throws Exception {
        //given
        FeedController c = new FeedController() {
            @Override
            protected Date getNow() {
                try {
                    return new SimpleDateFormat("yyyyMMdd").parse("20140501");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        EpisodeUtil u = new EpisodeUtil();
        ScheduledEpisodeProvider sp = new ScheduledEpisodeProvider();
        sp.setEntityManager(emf.createEntityManager());
        PersistentEpisodeProvider pp = new PersistentEpisodeProvider();
        pp.setEntityManager(emf.createEntityManager());
        u.setPersistentProvider(pp);
        u.setScheduledProvider(sp);
        c.setEpisodeUtil(u);
        c.setEntityManager(emf.createEntityManager());

        //when
        Feed feed = c.feed(1);

        //then
        JAXBContext jaxbc = JAXBContext.newInstance(Feed.class);
        Marshaller marshaller = jaxbc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(feed, System.out);

    }
}