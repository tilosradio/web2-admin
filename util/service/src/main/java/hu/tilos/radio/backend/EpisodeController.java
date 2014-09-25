package hu.tilos.radio.backend;

import hu.radio.tilos.model.Episode;
import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.types.EpisodeData;
import org.modelmapper.ModelMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/api/v1/episode")
public class EpisodeController {

    @Inject
    ModelMapper modelMapper;

    @Inject
    private EntityManager entityManager;

    @GET
    @Path("/{id}")
    @Security(role = Role.GUEST)
    @Produces("application/json")
    public EpisodeData get(@PathParam("id") int i) {
        EpisodeData r = modelMapper.map(entityManager.find(Episode.class, i), EpisodeData.class);
        return r;
    }

}
