package hu.tilos.radio.backend.episode;

import hu.radio.tilos.model.Episode;
import hu.radio.tilos.model.Scheduling;
import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.EpisodeData;
import hu.tilos.radio.backend.data.ShowSimple;
import org.dozer.DozerBeanMapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.DateFormat;
import java.util.*;

/**
 * Returns with the persisted episode records.
 */
public class ScheduledEpisodeProvider {

    private EntityManager entityManager;

    public List<EpisodeData> listEpisode(int showId, Date from, Date to) {
        DozerBeanMapper mapper = MappingFactory.createDozer(entityManager);

        String query = "SELECT s from Scheduling s WHERE s.validFrom < :end AND s.validTo > :start";
        if (showId > 0) {
            query += " AND s.show.id = :showId";
        }
        Query q = entityManager.createQuery(query);
        q.setParameter("start", from);
        q.setParameter("end", to);
        if (showId > 0) {
            q.setParameter("showId", showId);
        }
        List<Scheduling> schedulings = q.getResultList();

        List<EpisodeData> result = new ArrayList<>();
        for (Scheduling s : schedulings) {
            result.addAll(calculateEpisodes(s, from, to));
        }

        return result;

    }

    private List<EpisodeData> calculateEpisodes(Scheduling s, Date from, Date to) {
        DozerBeanMapper mapper = MappingFactory.createDozer(entityManager);
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        Calendar scheduledUntil = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        toCalendar.setTime(to);
        scheduledUntil.setTime(s.getValidTo());

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        c.set(from.getYear() + 1900, from.getMonth(), from.getDate(), s.getHourFrom(), s.getMinFrom(), 0);
        int offset = c.get(Calendar.DAY_OF_WEEK) - 2;
        if (offset < 0) {
            offset += 7;
        }
        c.add(Calendar.DAY_OF_MONTH, -1 * offset + (s.getWeekDay() - 1));
        c.set(Calendar.MILLISECOND, 0);

        List<EpisodeData> result = new ArrayList<>();
        while (c.compareTo(toCalendar) < 0 && c.compareTo(scheduledUntil) < 0) {
            if (isValidDate(c, s, from, to)) {
                //create episode from scheduling
                EpisodeData d = new EpisodeData();
                d.setPlannedFrom(c.getTime().getTime());
                d.setPlannedTo(d.getPlannedFrom() + s.getDuration() * 60 * 1000);
                d.setRealFrom(d.getPlannedFrom());
                d.setRealTo(d.getPlannedTo());
                d.setPersistent(false);
                d.setShow(mapper.map(s.getShow(), ShowSimple.class));
                result.add(d);
            }
            c.add(Calendar.DAY_OF_MONTH, 7);
        }
        return result;


    }

    protected boolean isValidDate(Calendar c, Scheduling s, Date from, Date to) {
        if (s.getWeekType() > 1) {
            int weekNo = (int) Math.floor((c.getTime().getTime() - s.getBase().getTime()) / (7 * 60 * 60 * 24));
            if (weekNo % s.getWeekType() != 0) {
                return false;
            }
        }
        Date realTime = c.getTime();
        if (realTime.compareTo(from) >= 0 && realTime.compareTo(to) < 0 && realTime.compareTo(s.getValidFrom()) >= 0 && realTime.compareTo(s.getValidTo()) < 0) {
            return true;
        }
        return false;
    }

    private Date weekStart(Date validFrom) {
        return null;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
