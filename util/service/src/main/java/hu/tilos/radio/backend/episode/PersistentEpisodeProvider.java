package hu.tilos.radio.backend.episode;


import hu.tilos.radio.backend.converters.MappingFactory;
import hu.tilos.radio.backend.data.types.EpisodeData;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hu.tilos.radio.backend.data.types.ShowSimple;
import hu.tilos.radio.backend.data.types.TextData;

import hu.tilos.radio.jooqmodel.Tables;
import hu.tilos.radio.jooqmodel.tables.pojos.Bookmark;
import hu.tilos.radio.jooqmodel.tables.pojos.Episode;
import hu.tilos.radio.jooqmodel.tables.pojos.Radioshow;
import hu.tilos.radio.jooqmodel.tables.pojos.Textcontent;
import hu.tilos.radio.jooqmodel.tables.records.TextcontentRecord;
import org.dozer.DozerBeanMapper;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import static hu.tilos.radio.jooqmodel.Tables.*;

/**
 * Returns with the persisted episode records.
 */
public class PersistentEpisodeProvider {

    @Resource
    private DataSource dataSource;

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

        Result<Record> resp = context.selectFrom(
                EPISODE.
                        join(RADIOSHOW).on(EPISODE.RADIOSHOW_ID.eq(RADIOSHOW.ID)).
                        join(TEXTCONTENT, JoinType.LEFT_OUTER_JOIN).on(EPISODE.TEXTCONTENT_ID.eq(TEXTCONTENT.ID)).
                        join(BOOKMARK, JoinType.LEFT_OUTER_JOIN).on(BOOKMARK.EPISODE_ID.eq(EPISODE.ID))
        ).where(whereCondition).fetch();


        Map<Integer, Result<Record>> episodes = resp.intoGroups(EPISODE.ID);
        for (Integer key : episodes.keySet()) {
            Result<Record> epiRecords = episodes.get(key);
            System.out.println(epiRecords);
            Episode e = epiRecords.get(0).into(Episode.class);
            EpisodeData ed = mapper.map(e, EpisodeData.class);
            TextData td = new TextData();

            //try to find data based on the bookmarks
            Map<Integer, Result<Record>> bookmarks = epiRecords.intoGroups(BOOKMARK.EPISODE_ID);
            Bookmark b = chooseTheBestBookmark(bookmarks.get(e.getId()));

            if (e.getTextcontentId() == null) {
                if (b != null) {
                    td.setTitle(b.getTitle());
                    td.setContent(b.getContent());
                }
            } else {
                //there is textContent
                td.setTitle(epiRecords.get(0).getValue(TEXTCONTENT.TITLE));
                td.setContent(epiRecords.get(0).getValue(TEXTCONTENT.CONTENT));
            }
            ed.setText(td);
            ed.setShow(mapper.map(epiRecords.get(0).into(Radioshow.class), ShowSimple.class));
            result.add(ed);

        }
//        .fetchInto(new RecordHandler<Record>() {
//            @Override
//            public void next(Record record) {
//                System.out.println(record);
//
//                d.setText(mapper.map(record.into(Textcontent.class), TextData.class));
//                d.setShow(mapper.map(record.into(Radioshow.class), ShowSimple.class));
//                result.add(d);
//            }
//        });

        return result;

    }

    private Bookmark chooseTheBestBookmark(Result<Record> bookmarks) {
        if (bookmarks.size() == 0) {
            return null;
        }
        Record r = (Record) bookmarks.get(0);
        Bookmark b = new Bookmark();
        b.setContent(r.getValue(BOOKMARK.CONTENT));
        b.setTitle(r.getValue(BOOKMARK.TITLE));
        b.setStart(r.getValue(BOOKMARK.START));
        b.setEnd(r.getValue(BOOKMARK.END));
        return b;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
