package hu.tilos.radio.backend.episode;

import hu.tilos.radio.backend.data.types.EpisodeData;

import java.util.Date;
import java.util.List;

public class EpisodeUtil {

    private PersistentEpisodeProvider persistentProvider;

    private ScheduledEpisodeProvider scheduledProvider;

    private Merger merger = new Merger();

    public List<EpisodeData> getEpisodeData(int showId, Date from, Date to) {
        return merger.merge(persistentProvider.listEpisode(showId, from, to), scheduledProvider.listEpisode(showId, from, to));
    }

    public PersistentEpisodeProvider getPersistentProvider() {
        return persistentProvider;
    }

    public void setPersistentProvider(PersistentEpisodeProvider persistentProvider) {
        this.persistentProvider = persistentProvider;
    }

    public ScheduledEpisodeProvider getScheduledProvider() {
        return scheduledProvider;
    }

    public void setScheduledProvider(ScheduledEpisodeProvider scheduledProvider) {
        this.scheduledProvider = scheduledProvider;
    }
}
