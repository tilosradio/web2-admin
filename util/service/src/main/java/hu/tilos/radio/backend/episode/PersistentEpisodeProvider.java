package hu.tilos.radio.backend.episode;


import hu.radio.tilos.model.Show;
import hu.radio.tilos.model.TextContent;
import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.EpisodeData;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.tilos.radio.backend.data.ShowSimple;
import hu.tilos.radio.backend.data.TextData;
import hu.tilos.radio.jooqmodel.Tables;
import hu.tilos.radio.jooqmodel.tables.Episode;
import hu.tilos.radio.jooqmodel.tables.pojos.Radioshow;
import hu.tilos.radio.jooqmodel.tables.pojos.Textcontent;
import hu.tilos.radio.jooqmodel.tables.records.EpisodeRecord;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import static hu.tilos.radio.jooqmodel.Tables.*;

/**
 * Returns with the persisted episode records.
 */
public class PersistentEpisodeProvider {

    private DataSource dataSource;

    public PersistentEpisodeProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<EpisodeData> listEpisode(int showId, Date from, Date to) {

        DSLContext context = DSL.using(dataSource, SQLDialect.MYSQL, new Settings().withRenderSchema(false));

        final DozerBeanMapper mapper = MappingFactory.createDozer(context);

        Condition whereCondition =
                EPISODE.PLANNEDFROM.lt(new Timestamp(to.getTime())).
                        and(
                                EPISODE.PLANNEDTO.gt(new Timestamp(from.getTime())));


        if (showId > 0) {
            whereCondition.and(RADIOSHOW.ID.eq(showId));
        } else {
            whereCondition.and(RADIOSHOW.STATUS.eq(1));
        }


        final List<EpisodeData> result = new ArrayList<>();

        context.selectFrom(
                EPISODE.
                        join(RADIOSHOW).on(EPISODE.RADIOSHOW_ID.eq(RADIOSHOW.ID)).
                        join(TEXTCONTENT, JoinType.LEFT_OUTER_JOIN).on(EPISODE.TEXTCONTENT_ID.eq(TEXTCONTENT.ID))
        ).where(whereCondition).fetchInto(new RecordHandler<Record>() {
            @Override
            public void next(Record record) {
                EpisodeData d = mapper.map(record.into(hu.tilos.radio.jooqmodel.tables.pojos.Episode.class), EpisodeData.class);
                d.setText(mapper.map(record.into(Textcontent.class), TextData.class));
                d.setShow(mapper.map(record.into(Radioshow.class), ShowSimple.class));
                result.add(d);
            }
        });

        return result;

    }


}
