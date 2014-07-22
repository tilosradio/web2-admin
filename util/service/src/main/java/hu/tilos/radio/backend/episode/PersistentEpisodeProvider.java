package hu.tilos.radio.backend.episode;

import hu.radio.tilos.model.Episode;
import hu.radio.tilos.model.Show;
import hu.radio.tilos.model.TextContent;
import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.EpisodeData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.tilos.radio.backend.data.ShowSimple;
import hu.tilos.radio.backend.data.TextData;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

/**
 * Returns with the persisted episode records.
 */
public class PersistentEpisodeProvider {

    private EntityManager entityManager;

    public List<EpisodeData> listEpisode(int showId, Date from, Date to) {
        DozerBeanMapper mapper = MappingFactory.createDozer(entityManager, new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Episode.class, EpisodeData.class).fields("text", "text").fields("show", "show");
                mapping(Show.class, ShowSimple.class);
                mapping(TextContent.class, TextData.class);
            }
        });

        Query q = entityManager.createQuery("SELECT e from Episode e WHERE e.plannedFrom < :end AND e.plannedTo > :start AND e.show.id = :showId");
        q.setParameter("start", from);
        q.setParameter("end", to);
        q.setParameter("showId", showId);
        List<Episode> episodes = q.getResultList();


        List<EpisodeData> result = new ArrayList<>();
        for (Episode e : episodes) {
            EpisodeData d = mapper.map(e, EpisodeData.class);
            d.setPersistent(true);
            result.add(d);
        }

        return result;

    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
