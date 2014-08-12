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
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generate atom feed for the shows.
 */
@Path("/feed")
public class FeedController {

    private static final SimpleDateFormat YYYY_DOT_MM_DOT_DD = new SimpleDateFormat("yyyy'.'MM'.'dd");

    private static final SimpleDateFormat YYYY_PER_MM_PER_DD = new SimpleDateFormat("yyyy'/'MM'/'dd");

    private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");

    private EntityManager entityManager;

    private EpisodeUtil episodeUtil;

    private String serverUrl;

    @GET
    @Path("/show/{alias}")
    @Security(role = Role.GUEST)
    @Produces("application/atom+xml")
    public Response feed(@PathParam("alias") String alias) {
        Feed feed = new Feed();
        try {
            Show show = null;
            try {
                show = (Show) entityManager.createQuery("SELECT s from Show s where s.alias = :alias").setParameter("alias", alias).getSingleResult();
            } catch (NoResultException ex) {
                return Response.status(Response.Status.NOT_FOUND).entity("Show is missing: " + alias).build();
            }

            feed.setTitle(show.getName() + " [Tilos Rádió podcast]");
            feed.setUpdated(new Date());


            Link showLink = new Link();
            showLink.setRel("self");
            showLink.setType(new MediaType("application", "atom+xml"));
            showLink.setHref(new URI(serverUrl + "/feed2/show/" + show.getAlias()));

            feed.getLinks().add(showLink);
            feed.setId(new URI("tilos:show/" + show.getAlias()));


            Date end = getNow();
            Date start = new Date();
            start.setTime(end.getTime() - (long) 60 * 24 * 30 * 60 * 1000);

            Person p = new Person();
            p.setEmail("info@tilos.hu");
            p.setName("Tilos Rádió");
            List<Person> authors = new ArrayList();
            authors.add(p);

            for (EpisodeData episode : episodeUtil.getEpisodeData(show.getId(), start, end)) {
                try {
                    Entry e = new Entry();
                    if (episode.getText() != null) {
                        e.setTitle(YYYY_DOT_MM_DOT_DD.format(episode.getPlannedFrom()) + " " + episode.getText().getTitle());
                        e.setSummary(episode.getText().getContent());
                    } else {
                        e.setTitle(YYYY_DOT_MM_DOT_DD.format(episode.getPlannedFrom()) + " " + "adásnapló");
                        e.setSummary("adás archívum");
                    }


                    e.setPublished(dateFromEpoch(episode.getRealTo()));
                    e.setUpdated(dateFromEpoch(episode.getRealTo()));

                    URL url = new URL(serverUrl + "/episode/" + show.getAlias() + "/" + YYYY_PER_MM_PER_DD.format(e.getPublished()));

                    e.setId(url.toURI());

                    Link alternate = new Link();
                    alternate.setRel("alternate");
                    alternate.setType(MediaType.TEXT_HTML_TYPE);
                    alternate.setHref(url.toURI());
                    e.getLinks().add(alternate);

                    Link sound = new Link();
                    sound.setType(new MediaType("audio", "mpeg"));
                    sound.setRel("enclosure");
                    sound.setHref(new URI(createDownloadURI(episode)));
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
        return Response.ok().entity(feed).build();
    }

    public static String createDownloadURI(EpisodeData episode) {
        return "http://tilos.hu/mp3/tilos-" +
                YYYYMMDD.format(dateFromEpoch(episode.getRealFrom())) + "-" +
                HHMMSS.format(dateFromEpoch(episode.getRealFrom())) + "-" +
                HHMMSS.format(dateFromEpoch(episode.getRealTo())) + ".mp3";
    }

    private static Date dateFromEpoch(long realTo) {
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

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
