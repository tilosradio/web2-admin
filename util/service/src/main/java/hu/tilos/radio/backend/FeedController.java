package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.types.EpisodeData;
import hu.tilos.radio.backend.episode.EpisodeUtil;
import hu.tilos.radio.jooqmodel.tables.daos.RadioshowDao;
import hu.tilos.radio.jooqmodel.tables.pojos.Radioshow;
import net.anzix.jaxrs.atom.*;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
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

    @Inject
    private EpisodeUtil episodeUtil;

    @Inject
    @ConfigProperty(name = "server.url")
    private String serverUrl;

    @Resource
    private DataSource dataSource;

    private Configuration createConfiguration() {
        return new DefaultConfiguration().set(dataSource).set(SQLDialect.MYSQL).set(new Settings().withRenderSchema(false));
    }

    @GET
    @Path("/show/{alias}")
    @Security(role = Role.GUEST)
    @Produces("application/atom+xml")
    public Response feed(@PathParam("alias") String alias) {
        Feed feed = new Feed();
        try {
            List<Radioshow> result = new RadioshowDao(createConfiguration()).fetchByAlias(alias);
            if (result.size() < 1) {
                return Response.status(Response.Status.NOT_FOUND).entity("Show is missing: " + alias).build();
            }
            Radioshow show = result.get(0);

            feed.setTitle(show.getName() + " [Tilos Rádió podcast]");
            feed.setUpdated(new Date());


            Link feedLink = new Link();
            feedLink.setRel("self");
            feedLink.setType(new MediaType("application", "atom+xml"));
            feedLink.setHref(new URI(serverUrl + "/feed/show/" + show.getAlias()));

            feed.getLinks().add(feedLink);
            feed.setId(new URI("http://tilos.hu/show/" + show.getAlias()));


            Date end = getNow();
            Date start = new Date();
            //three monthes
            start.setTime(end.getTime() - (long) 60 * 24 * 30 * 3 * 60 * 1000);

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
                        e.setSummary(new Summary("html", episode.getText().getContent()));
                    } else {
                        e.setTitle(YYYY_DOT_MM_DOT_DD.format(episode.getPlannedFrom()) + " " + "adásnapló");
                        e.setSummary(new Summary("adás archívum"));
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
