package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;
import hu.tilos.radio.backend.data.UserInfo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Generate atom feed for the shows.
 */
@Path("/api/v1/user")
public class UserController {


    @Inject
    private EntityManager entityManager;

    @Inject
    Session session;

    @Path("/me")
    @Produces("application/json")
    @Security(role = Role.GUEST)
    @GET
    public UserInfo me() {
        return session.getCurrentUser();
    }


}
