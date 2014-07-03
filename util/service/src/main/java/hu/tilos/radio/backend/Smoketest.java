package hu.tilos.radio.backend;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/v1")
public class Smoketest {

    @GET
    @Path("ping")
    public String ping() {
        return "pong";
    }

}
