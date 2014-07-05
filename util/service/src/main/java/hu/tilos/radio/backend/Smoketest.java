package hu.tilos.radio.backend;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Smoketest {

    @GET
    @Path("v1/ping")
    public String ping() {
        return "pong";
    }

}
