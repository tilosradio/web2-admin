package hu.tilos.radio.backend;

import hu.radio.tilos.model.Role;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Smoketest {

    @GET
    @Path("v1/ping")
    @Security(role = Role.GUEST)
    public String ping() {
        return "pong";
    }

}
