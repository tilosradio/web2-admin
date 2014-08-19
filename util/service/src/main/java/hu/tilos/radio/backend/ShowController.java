package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.types.AuthorSimple;
import hu.tilos.radio.backend.data.types.Contributor;
import hu.tilos.radio.backend.data.types.MixSimple;
import hu.tilos.radio.backend.data.types.ShowDetailed;
import hu.tilos.radio.jooqmodel.Tables;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.modelmapper.ModelMapper;
import org.modelmapper.jooq.RecordValueReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static hu.tilos.radio.jooqmodel.Tables.*;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

@Path("/api/v1/show")
public class ShowController {

    @Resource
    private DataSource datasource;

    private static Logger LOG = LoggerFactory.getLogger(ShowController.class);

    private Configuration createConfiguration() {
        return new DefaultConfiguration().set(datasource).set(SQLDialect.MYSQL).set(new Settings().withRenderSchema(false));
    }

    @Produces("application/json")
    @Path("/{alias}")
    @Security(role = Role.GUEST)
    @GET
    public ShowDetailed get(@PathParam("alias") String alias) {
        DSLContext context = DSL.using(datasource, SQLDialect.MYSQL, new Settings().withRenderSchema(false));
        LOG.info("Started");
        LOG.info("Datasource: " + datasource);

        SelectConditionStep<Record> query = context.selectFrom(
                RADIOSHOW.
                        join(MIX, JoinType.LEFT_OUTER_JOIN).on(MIX.SHOW_ID.eq(RADIOSHOW.ID)).
                        join(CONTRIBUTION, JoinType.LEFT_OUTER_JOIN).on(CONTRIBUTION.RADIOSHOW_ID.eq(RADIOSHOW.ID)).
                        join(AUTHOR, JoinType.LEFT_OUTER_JOIN).on(AUTHOR.ID.eq(CONTRIBUTION.AUTHOR_ID))).
                where(RADIOSHOW.ALIAS.eq(alias));

        System.out.println(query.getSQL());
        Result<Record> result = query.fetch();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());

        ShowDetailed detailed = modelMapper.map(result.get(0), ShowDetailed.class);

        if (result.get(0).getValue(MIX.ID) != null) {
            for (Result<Record> r : result.intoGroups(MIX.SHOW_ID).get(detailed.getId()).intoGroups(MIX.ID).values()) {
                detailed.getMixes().add(modelMapper.map(r.get(0), MixSimple.class));
            }
        }

//        for (Result<Record> r : result.intoGroups(CONTRIBUTION.RADIOSHOW_ID).get(detailed.getId()).intoGroups(CONTRIBUTION.ID).values()) {
//            Contributor c = modelMapper.map(r, Contributor.class);
//            c.setAuthor(modelMapper.map(r, AuthorSimple.class));
//            detailed.getContributors().add(c);
//        }


        return detailed;
////        Radioshow show = dao.fetchByAlias(alias);
////
////        BeanMappingBuilder builder = new BeanMappingBuilder() {
////
////            @Override
////            protected void configure() {
////                mapping(Show.class, ShowDetailed.class).fields("status", "statusTxt", customConverter(ShowStatusConverter.class.getCanonicalName())).exclude("description");
////            }
////        };
////        DozerBeanMapper mapper = new DozerBeanMapper();
////        mapper.addMapping(builder);
////        ShowDetailed response = mapper.map(show, ShowDetailed.class);
////
////        em.close();
//        return response;

    }

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }
}
