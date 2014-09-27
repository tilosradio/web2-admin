package hu.tilos.radio.backend.episode;


import hu.radio.tilos.model.Episode;
import hu.radio.tilos.model.Show;
import hu.radio.tilos.model.TextContent;
import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.types.EpisodeData;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hu.tilos.radio.backend.data.types.ShowSimple;
import hu.tilos.radio.backend.data.types.TextData;


import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.modelmapper.ModelMapper;


/**
 * Returns with the persisted episode records.
 */
public class PersistentEpisodeProvider {

    @Inject
    private EntityManager entityManager;

    @Inject
    private ModelMapper modelMapper;

    public List<EpisodeData> listEpisode(int showId, Date from, Date to) {

        String query = "SELECT e from Episode e WHERE e.plannedFrom < :end AND e.plannedTo > :start";


        if (showId > 0) {
            query += " AND e.show.id = :showId";
        } else {
            query += " AND e.show.status = 1";

        }
        TypedQuery<Episode> q = entityManager.createQuery(query, Episode.class);
        q.setParameter("start", from);
        q.setParameter("end", to);
        if (showId > 0) {
            q.setParameter("showId", showId);
        }

        List<EpisodeData> result = new ArrayList<>();
        for (Episode e : q.getResultList()) {
            EpisodeData d = modelMapper.map(e, EpisodeData.class);
            d.setPersistent(true);
            result.add(d);
        }


        return result;

    }

//    private Bookmark chooseTheBestBookmark(Result<Record> bookmarks) {
//        if (bookmarks.size() == 0) {
//            return null;
//        }
//        Record r = (Record) bookmarks.get(0);
//        Bookmark b = new Bookmark();
//        b.setContent(r.getValue(BOOKMARK.CONTENT));
//        b.setTitle(r.getValue(BOOKMARK.TITLE));
//        b.setStart(r.getValue(BOOKMARK.START));
//        b.setEnd(r.getValue(BOOKMARK.END));
//        return b;
//    }


    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
