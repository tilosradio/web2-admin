package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.radio.tilos.model.Scheduling;
import hu.tilos.radio.backend.data.EpisodeData;
import hu.tilos.radio.backend.episode.EpisodeUtil;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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
    @Produces("audio/x-mpegurl; charset=iso-8859-2")
    @Security(role = Role.GUEST)
    public Response lastWeek() {
        Date now = new Date();
        Date weekAgo = new Date();
        weekAgo.setTime(now.getTime() - (long) 604800000L);
        List<EpisodeData> episodes = episodeUtil.getEpisodeData(-1, weekAgo, now);

        Collections.sort(episodes, new Comparator<EpisodeData>() {
            @Override
            public int compare(EpisodeData o1, EpisodeData o2) {
                return -1 * Long.valueOf(o1.getRealFrom()).compareTo(Long.valueOf(o2.getRealFrom()));
            }
        });

        episodes.remove(0);

        StringBuilder result = new StringBuilder();
        result.append("#EXTM3U\n");
        result.append("#EXTINF:-1, Tilos Rádió - élő adás (256kb/s) \n");
        result.append("http://stream.tilos.hu/tilos\n");
        result.append("#EXTINF:-1, Tilos Rádió - [CSAKASZAVAK] Szöveges archívum \n");
        result.append("http://stream.tilos.hu/csakaszavak.ogg\n");
        result.append("#EXTINF:-1, Tilos Rádió - [CSAKAZENE] Zenés archívum\n");
        result.append("http://stream.tilos.hu/csakazene.ogg\n");
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
        return Response.ok(result.toString()).build();
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
