package se.soderman.simpleblog.configuration;


import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import se.soderman.simpleblog.controller.UserController;
import se.soderman.simpleblog.exception.ExceptionMapper;
import se.soderman.simpleblog.security.AuthenticationFilter;


@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(UserController.class);
        register(AuthenticationFilter.class);

        property("jersey.config.server.provider.packages", "se.soderman.simpleblog");
    }
}
