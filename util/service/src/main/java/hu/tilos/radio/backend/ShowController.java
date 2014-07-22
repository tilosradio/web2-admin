package hu.tilos.radio.backend;

import hu.radio.tilos.model.Show;
import hu.tilos.radio.backend.converters.ShowStatusConverter;
import hu.tilos.radio.backend.data.ShowDetailed;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.DozerBuilder;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOption;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

@Path("/api/v1/show")
public class ShowController {

    private EntityManagerFactory emf;

    @Produces("application/json")
    @GET
    public ShowDetailed list() {

        EntityManager em = emf.createEntityManager();
        Show show = em.find(Show.class, 485L);

        BeanMappingBuilder builder = new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(Show.class, ShowDetailed.class).fields("status", "statusTxt", customConverter(ShowStatusConverter.class.getCanonicalName())).exclude("description");
            }
        };
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(builder);
        ShowDetailed response = mapper.map(show, ShowDetailed.class);

        em.close();
        return response;

    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
