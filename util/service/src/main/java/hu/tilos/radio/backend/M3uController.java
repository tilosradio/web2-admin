package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.EpisodeData;
import hu.tilos.radio.backend.episode.EpisodeUtil;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Generate various m3u feeds.
 */
@Path("/api/v1/m3u")
public class M3uController {

    private EntityManager entityManager;

    private EpisodeUtil episodeUtil;

    private static final SimpleDateFormat YYYY_DD_MM = new SimpleDateFormat("yyyy'.'MM'.'dd");

    @GET
    @Path("lastweek")
    @Produces("text/plain")
    @Security(role = Role.GUEST)
    public Response lastWeek() {
        Date now = new Date();
        Date weekAgo = new Date();
        weekAgo.setTime(now.getTime() - (long) 604800000L);
        List<EpisodeData> episodes = episodeUtil.getEpisodeData(-1, weekAgo, now);

        StringBuilder result = new StringBuilder();
        result.append("#EXTM3U\n");
        for (EpisodeData episode : episodes) {
            String artist = episode.getShow().getName();
            String title = YYYY_DD_MM.format(episode.getPlannedFrom());
            if (episode.getText() != null) {
                title += " " + episode.getText().getTitle();
            } else {
                title += " adás archívum";
            }
            result.append("#EXTINF:-1, " + artist + " - " + title + "\n");
            result.append(FeedController.createDownloadURI(episode) + "\n");
        }
        return Response.ok(result.toString()).header("Content-Type","audio/x-mpegurl; charset=utf-8").build();
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
