package hu.tilos.radio.backend.episode;

import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.types.EpisodeData;
import hu.tilos.radio.backend.data.types.ShowSimple;
import hu.tilos.radio.jooqmodel.tables.pojos.Radioshow;
import hu.tilos.radio.jooqmodel.tables.pojos.Scheduling;
import org.dozer.DozerBeanMapper;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;
import java.util.Date;

import static hu.tilos.radio.jooqmodel.Tables.*;

/**
 * Returns with the persisted episode records.
 */
public class ScheduledEpisodeProvider {

    @Resource
    private DataSource dataSource;

    public List<EpisodeData> listEpisode(int showId, final Date from, final Date to) {
        final DSLContext context = DSL.using(dataSource, SQLDialect.MYSQL, new Settings().withRenderSchema(false));
        final DozerBeanMapper mapper = MappingFactory.createDozer(context);


        Condition whereCondition = SCHEDULING.VALIDFROM.lt(new java.sql.Date(to.getTime()));
        whereCondition = whereCondition.and(SCHEDULING.VALIDTO.gt(new java.sql.Date(from.getTime())));

        if (showId > 0) {
            whereCondition = whereCondition.and(RADIOSHOW.ID.eq(showId));
        } else {
            whereCondition = whereCondition.and(RADIOSHOW.STATUS.eq(1));
        }


        final List<EpisodeData> result = new ArrayList<>();

        context.selectFrom(
                SCHEDULING.join(RADIOSHOW).onKey()
                ).where(whereCondition).
                fetchInto(new RecordHandler<Record>() {
                    @Override
                    public void next(Record record) {
                        result.addAll(calculateEpisodes(context,
                                record.into(Scheduling.class),
                                record.into(Radioshow.class),
                                from,
                                to));
                    }
                });


//        String query = "SELECT s from Scheduling s WHERE s.validFrom < :end AND s.validTo > :start";
//        if (showId > 0) {
//            query += " AND s.show.id = :showId";
//        } else {
//            query += " AND s.show.status = 1";
//        }
//        Query q = entityManager.createQuery(query);
//        q.setParameter("start", from);
//        q.setParameter("end", to);
//        if (showId > 0) {
//            q.setParameter("showId", showId);
//        }
//        List<Scheduling> schedulings = q.getResultList();
//
//        List<EpisodeData> result = new ArrayList<>();
//        for (Scheduling s : schedulings) {
//
//        }

        return result;

    }

    private List<EpisodeData> calculateEpisodes(DSLContext jooq, Scheduling s, Radioshow show, Date from, Date to) {
        DozerBeanMapper mapper = MappingFactory.createDozer(jooq);
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        Calendar scheduledUntil = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        toCalendar.setTime(to);
        scheduledUntil.setTime(s.getValidto());

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        c.set(from.getYear() + 1900, from.getMonth(), from.getDate(), s.getHourfrom(), s.getMinfrom(), 0);
        int offset = c.get(Calendar.DAY_OF_WEEK) - 2;
        if (offset < 0) {
            offset += 7;
        }
        c.add(Calendar.DAY_OF_MONTH, -1 * offset + s.getWeekday());
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
                d.setShow(mapper.map(show, ShowSimple.class));
                result.add(d);
            }
            c.add(Calendar.DAY_OF_MONTH, 7);
        }
        return result;


    }

    protected boolean isValidDate(Calendar c, Scheduling s, Date from, Date to) {
        if (s.getWeektype() > 1) {
            int weekNo = (int) Math.floor((c.getTime().getTime() - s.getBase().getTime()) / (7000l * 60 * 60 * 24));
            if (weekNo % s.getWeektype() != 0) {
                return false;
            }
        }
        Long realTime = c.getTime().getTime();
        Long toTime = to.getTime();
        Long fromTime = from.getTime();
        Long validFromTime = s.getValidfrom().getTime();
        Long validToTime = s.getValidto().getTime();

        if (realTime.compareTo(fromTime) >= 0 && realTime.compareTo(toTime) < 0 && realTime.compareTo(validFromTime) >= 0 && realTime.compareTo(validToTime) < 0) {
            return true;
        }
        return false;
    }

    private Date weekStart(Date validFrom) {
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
