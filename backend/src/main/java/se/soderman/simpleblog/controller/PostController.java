package se.soderman.simpleblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.soderman.simpleblog.dao.PostRepository;
import se.soderman.simpleblog.dao.PostSearch;
import se.soderman.simpleblog.domain.Post;
import se.soderman.simpleblog.exception.NotFoundException;
import se.soderman.simpleblog.security.SecuredEndpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static se.soderman.simpleblog.constant.Paths.ADMIN;
import static se.soderman.simpleblog.constant.Paths.POSTS;

@Service
@Path(ADMIN + POSTS)
public class PostController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final PostRepository postRepository;

    private final PostSearch postSearch;


    @Autowired
    public PostController(PostRepository postRepository, PostSearch postSearch) {
        this.postRepository = postRepository;
        this.postSearch = postSearch;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Iterable<Post> getAllPosts() throws Exception {
        return postRepository.findAllByOrderByDateDesc();
    };

    @POST
    @Consumes({ MediaType.APPLICATION_JSON  })
    @Produces({ MediaType.APPLICATION_JSON })
    @SecuredEndpoint
    public Post createPost(Post post) throws Exception {
        return postRepository.save(post);
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Post getPost(@PathParam("id") Integer id) throws Exception {
        return postRepository.findById(id).orElseThrow(() -> new NotFoundException("Unknown resource", String.valueOf(id)));
    };

    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.TEXT_PLAIN })
    @SecuredEndpoint
    public String deletePost(@PathParam("id") Integer id) throws Exception {
        Post p = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Unknown resource", String.valueOf(id)));
        postRepository.delete(p);
        return String.valueOf(id);
    };

    @GET
    @Path("/date/{date}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Iterable<Post> getAllPostsForDate(@PathParam("date") String date) throws Exception {
        Date d = sdf.parse(date);
        Date end = new Date(d.getTime() + 1000*60*60*24);
        return postRepository.findByDateBetween(d, end);
    };

    @GET
    @Path("/search")
    @Produces({ MediaType.APPLICATION_JSON })
    public Iterable<Post> search(@QueryParam("query") String query) {
        List searchResults = postSearch.search(query);
        return searchResults;
    }
}
