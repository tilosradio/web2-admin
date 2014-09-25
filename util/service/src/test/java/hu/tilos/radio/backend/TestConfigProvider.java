package hu.tilos.radio.backend;

import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.enterprise.inject.Produces;

public class TestConfigProvider {

    @ConfigProperty(name = "server.url")
    @Produces
    public String getUrl() {
        return "http://tilos.hu";
    }
}
