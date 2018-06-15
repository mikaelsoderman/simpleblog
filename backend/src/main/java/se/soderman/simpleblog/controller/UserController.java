package se.soderman.simpleblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.domain.BlogUser;
import se.soderman.simpleblog.exception.AuthenticationException;
import se.soderman.simpleblog.security.SecuredEndpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static se.soderman.simpleblog.constant.Paths.ADMIN;
import static se.soderman.simpleblog.constant.Paths.USERS;


@Service
@Path(ADMIN + USERS)
public class UserController {

    private final BlogUserRepository userRepository;
    private final MessageSource messageSource;

    @Autowired
    public UserController(BlogUserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @SecuredEndpoint
    public Iterable<BlogUser> getAllUsers() {
        return userRepository.findAll();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON  })
    @Produces({ MediaType.APPLICATION_JSON })
    public BlogUser createUser(BlogUser user) {
        return userRepository.save(user);
    }

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON })
    public BlogUser login(String json) throws Exception {

        Map<String, String> response = new ObjectMapper().readValue(json, HashMap.class);
        String email = response.get("email");
        String password = response.get("password");
        BlogUser user = userRepository.findByEmail(email);
        boolean isAuthOk = user != null && user.getEmail().equals(email) && user.getPassword().equals(password);

        if (isAuthOk) {
            return user;
        } else {
            throw new AuthenticationException(messageSource.getMessage("login.failed", null, Locale.getDefault()), email);
        }
    }

}
