package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.radio.tilos.model.Show;
import hu.tilos.radio.backend.data.EpisodeData;
import hu.tilos.radio.backend.episode.EpisodeUtil;
import org.jboss.resteasy.plugins.providers.atom.Entry;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.plugins.providers.atom.Link;
import org.jboss.resteasy.plugins.providers.atom.Person;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/feed2")
public class FeedController {

    private static final SimpleDateFormat YYYY_DOT_MM_DOT_DD = new SimpleDateFormat("yyyy'.'MM'.'dd");

    private static final SimpleDateFormat YYYY_PER_MM_PER_DD = new SimpleDateFormat("yyyy'/'MM'/'dd");

    private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");

    private EntityManager entityManager;

    private EpisodeUtil episodeUtil;

    @GET
    @Path("/show/{id}")
    @Security(role = Role.GUEST)
    @Produces("application/atom+xml")
    public Feed feed(@PathParam("id") int id) {
        Feed feed = new Feed();
        try {
            Show show = entityManager.find(Show.class, id);
            if (show == null) {
                //todo;
            }

            feed.setTitle(show.getName() + " [Tilos Rádió podcast]");
            feed.setUpdated(new Date());

            Link showLink = new Link();
            showLink.setRel("self");
            showLink.setHref(new URI("http://tilos.hu/show/" + show.getAlias()));
            feed.getLinks().add(showLink);


            Date end = getNow();
            Date start = new Date();
            start.setTime(end.getTime() - (long) 60 * 24 * 30 * 60 * 1000);

            Person p = new Person();
            p.setEmail("info@tilos.hu");
            p.setName("Tilos Rádió");
            List<Person> authors = new ArrayList();
            authors.add(p);

            for (EpisodeData episode : episodeUtil.getEpisodeData(id, start, end)) {
                try {
                    Entry e = new Entry();
                    if (episode.getText() != null) {
                        e.setTitle(YYYY_DOT_MM_DOT_DD.format(episode.getPlannedFrom()) + " " + episode.getText().getTitle());
                        e.setSummary(episode.getText().getContent());
                    } else {
                        e.setTitle(YYYY_DOT_MM_DOT_DD.format(episode.getPlannedFrom()) + " " + "adásnapló");
                        e.setSummary("");
                    }


                    e.setPublished(dateFromEpoch(episode.getRealTo()));
                    e.setUpdated(dateFromEpoch(episode.getRealTo()));

                    URL url = new URL("http://tilos.hu/episode/" + show.getAlias() + "/" + YYYY_PER_MM_PER_DD.format(e.getPublished()));

                    e.setId(url.toURI());

                    Link alternate = new Link();
                    alternate.setRel("alternate");
                    alternate.setType(MediaType.TEXT_HTML_TYPE);
                    alternate.setHref(url.toURI());
                    e.getLinks().add(alternate);

                    Link sound = new Link();
                    sound.setType(new MediaType("audio", "mpeg"));
                    sound.setRel("enclosure");
                    sound.setHref(new URI("http://tilos.hu/mp3/tilos-" +
                            YYYYMMDD.format(dateFromEpoch(episode.getRealFrom())) + "-" +
                            HHMMSS.format(dateFromEpoch(episode.getRealFrom())) + "-" +
                            HHMMSS.format(dateFromEpoch(episode.getRealTo())) + ".mp3"));
                    e.getLinks().add(sound);


                    e.getAuthors().addAll(authors);

                    feed.getEntries().add(e);
                } catch (MalformedURLException e1) {
                    throw new RuntimeException(e1);
                } catch (URISyntaxException e1) {
                    throw new RuntimeException(e1);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //TODO
        }
        return feed;
    }

    private Date dateFromEpoch(long realTo) {
        Date d = new Date();
        d.setTime(realTo);
        return d;
    }

    protected Date getNow() {
        return new Date();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EpisodeUtil getEpisodeUtil() {
        return episodeUtil;
    }

    public void setEpisodeUtil(EpisodeUtil episodeUtil) {
        this.episodeUtil = episodeUtil;
    }
}
