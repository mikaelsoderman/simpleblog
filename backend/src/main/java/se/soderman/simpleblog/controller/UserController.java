package se.soderman.simpleblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.domain.BlogUser;
import se.soderman.simpleblog.security.SecuredEndpoint;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static se.soderman.simpleblog.constant.Paths.ADMIN;
import static se.soderman.simpleblog.constant.Paths.USERS;


@Service
@Path(ADMIN + USERS)
public class UserController {

    @Autowired
    BlogUserRepository userRepository;

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @SecuredEndpoint
    public Iterable<BlogUser> getAllUsers() throws Exception {
        return userRepository.findAll();
    };

    @POST
    @Consumes({ MediaType.APPLICATION_JSON  })
    @Produces({ MediaType.APPLICATION_JSON })
    public BlogUser createUser(BlogUser user) throws Exception {
        return userRepository.save(user);
    }

}
