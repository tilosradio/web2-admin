package hu.tilos.radio.backend;

import hu.radio.tilos.model.Mix;
import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.converters.*;
import hu.tilos.radio.backend.data.CreateResponse;
import hu.tilos.radio.backend.data.MixRequest;
import hu.tilos.radio.backend.data.MixResponse;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.DozerBuilder;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOption;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;

import java.util.ArrayList;
import java.util.List;

import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

@Path("/api/v1/mix")
public class MixController {

    @PersistenceContext
    private EntityManager entityManager;

    BeanMappingBuilder retrieveBuilder = new BeanMappingBuilder() {

        @Override
        protected void configure() {
            mapping(Mix.class, MixResponse.class)
                    .fields("type", "typeText", new FieldsMappingOption() {
                        @Override
                        public void apply(DozerBuilder.FieldMappingBuilder fieldMappingBuilder) {
                            fieldMappingBuilder.customConverter(EntityTextConverter.class);
                            fieldMappingBuilder.customConverterParam("Beszélgetés,Zene");
                        }
                    })
                    .fields("type", "type")
                    .fields("date", "date", new FieldsMappingOption() {
                        @Override
                        public void apply(DozerBuilder.FieldMappingBuilder fieldMappingBuilder) {
                            fieldMappingBuilder.customConverter(DateToTextConverter.class);
                            fieldMappingBuilder.customConverterParam("yyyy-MM-dd");
                        }
                    });
        }
    };

    BeanMappingBuilder updateBuilder = new BeanMappingBuilder() {

        @Override
        protected void configure() {
            mapping(MixRequest.class, Mix.class)
                    .fields("show", "show", new FieldsMappingOption() {
                        @Override
                        public void apply(DozerBuilder.FieldMappingBuilder fieldMappingBuilder) {
                            fieldMappingBuilder.customConverterId(ChildEntityFieldConverter.ID);
                        }
                    })
                    .fields("date", "date", new FieldsMappingOption() {
                        @Override
                        public void apply(DozerBuilder.FieldMappingBuilder fieldMappingBuilder) {
                            fieldMappingBuilder.customConverter(DateToTextConverter.class);
                            fieldMappingBuilder.customConverterParam("yyyy-MM-dd");
                        }
                    })
                    .exclude("id");


        }
    };

    @Produces("application/json")
    @Security(role = Role.GUEST)
    @GET
    public List<MixResponse> list(@QueryParam("show") String show) {

        String query = "SELECT m from Mix m";
        if (show != null) {
            query += " LEFT JOIN m.show s WHERE s.alias = :alias";
        }
        Query q = entityManager.createQuery(query, Mix.class);
        if (show != null) {
            q.setParameter("alias", show);
        }
        List<Mix> mixes = q.getResultList();

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(retrieveBuilder);

        List<MixResponse> response = new ArrayList<>();
        for (Mix mix : mixes) {
            response.add(mapper.map(mix, MixResponse.class));
        }

        return response;

    }

    @Produces("application/json")
    @Security(role = Role.ADMIN)
    @POST
    public CreateResponse create(MixRequest newMix) {

        DozerBeanMapper mapper = MappingFactory.createDozer(entityManager, updateBuilder);

        Mix mix = mapper.map(newMix, Mix.class);

        entityManager.persist(mix);

        return new CreateResponse(mix.getId());

    }

    @Produces("application/json")
    @Security(role = Role.ADMIN)
    @PUT
    @Path("/{id}")
    public CreateResponse update(@PathParam("id") int id, MixRequest newMix) {

        Mix mix = entityManager.find(Mix.class, id);

        DozerBeanMapper mapper = MappingFactory.createDozer(entityManager, updateBuilder);

        mapper.map(newMix, mix);

        return new CreateResponse(mix.getId());
    }


    @GET
    @Path("/{id}")
    @Security(role = Role.GUEST)
    @Produces("application/json")
    public MixResponse get(@PathParam("id") int i) {

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(retrieveBuilder);
        MixResponse r = mapper.map(entityManager.find(Mix.class, i), MixResponse.class);

        return r;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
