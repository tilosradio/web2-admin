package hu.tilos.radio.backend;

import hu.radio.tilos.model.Mix;
import hu.radio.tilos.model.Role;
import hu.radio.tilos.model.Show;
import hu.tilos.radio.backend.data.MixResponse;
import hu.tilos.radio.backend.data.types.AuthorSimple;
import hu.tilos.radio.backend.data.types.MixSimple;
import hu.tilos.radio.backend.data.types.ShowDetailed;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.jooq.RecordValueReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static hu.tilos.radio.jooqmodel.Tables.*;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

@Path("/api/v1/show")
public class ShowController {

    private static Logger LOG = LoggerFactory.getLogger(ShowController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Produces("application/json")
    @Path("/{alias}")
    @Security(role = Role.GUEST)
    @GET
    public ShowDetailed get(@PathParam("alias") String alias) {


        TypedQuery<Show> query = entityManager.createQuery("SELECT s FROM Show s " +
                "LEFT JOIN FETCH s.mixes " +
                "LEFT JOIN FETCH s.contributors " +
                "WHERE s.alias=:alias", Show.class);
        query.setParameter("alias", alias);


        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Mix, MixSimple>() {
            @Override
            protected void configure() {
                map().setType(source.getTypeCode());
            }
        });

        Show show = query.getSingleResult();
        ShowDetailed detailed = modelMapper.map(show, ShowDetailed.class);

        return detailed;

    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
