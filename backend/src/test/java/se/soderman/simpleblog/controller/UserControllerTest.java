package se.soderman.simpleblog.controller;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import se.soderman.simpleblog.BackendApplication;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.dao.PostRepository;
import se.soderman.simpleblog.domain.BlogUser;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    BlogUserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    private BlogUser adminUser, anotherUser;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headersWithAuth;

    @Before
    public void setUp() throws Exception {

        postRepository.deleteAll();
        userRepository.deleteAll();

        String credentials = new String(Base64.encode("testuser@test.se:Qwerty123".getBytes()));
        headersWithAuth = new HttpHeaders();
        headersWithAuth.add("Authorization", "Basic"+credentials);
        BlogUser user = new BlogUser();
        user.setName("Test User");
        user.setEmail("testuser@test.se");
        user.setPassword("Qwerty123");
        adminUser = userRepository.save(user);
        BlogUser user2 = new BlogUser();
        user2.setName("Another User");
        user2.setEmail("user2@test.se");
        user2.setPassword("Asdfg987");
        anotherUser = userRepository.save(user2);
    }

    @Test
    public void getAllUsers_ShouldReturnListWithAllUsers_WhenAuthHeadersAreCorrect() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headersWithAuth);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.GET, entity, String.class);

        assertEquals("Invalid response code", 200, response.getStatusCodeValue());
        String expected = "[{\"id\":" + adminUser.getId() + ",\"email\":\"testuser@test.se\",\"password\":\"Qwerty123\",\"name\":\"Test User\"},{\"id\":" + anotherUser.getId() + ",\"email\":\"user2@test.se\",\"password\":\"Asdfg987\",\"name\":\"Another User\"}]";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void getAllUsers_ShouldReturnStatusCode401_WhenAuthHeadersContainWrongPassword() throws Exception {
        String credentials = new String(Base64.encode("testuser@test.se:qwerty123".getBytes()));
        HttpHeaders headersWithWrongPass = new HttpHeaders();
        headersWithWrongPass.add("Authorization", "Basic"+credentials);
        HttpEntity<String> entity = new HttpEntity<>(null, headersWithWrongPass);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.GET, entity, String.class);

        assertEquals("Invalid response code", 401, response.getStatusCodeValue());
        String expected = "{\"httpStatus\":401,\"messages\":[\"Unauthorized\"]}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void getAllUsers_ShouldReturnStatusCode401_WhenAuthHeadersContainUnknownUser() throws Exception {
        String credentials = new String(Base64.encode("no.such.user@test.se:qwerty123".getBytes()));
        HttpHeaders headersWithWrongPass = new HttpHeaders();
        headersWithWrongPass.add("Authorization", "Basic"+credentials);
        HttpEntity<String> entity = new HttpEntity<>(null, headersWithWrongPass);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.GET, entity, String.class);

        assertEquals("Invalid response code", 401, response.getStatusCodeValue());
        String expected = "{\"httpStatus\":401,\"messages\":[\"Unauthorized\"]}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    @Test
    public void getAllUsers_ShouldReturnStatusCode401_WhenHeadersContainNoAuth() throws Exception {

        HttpHeaders headersWitoutAuth = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headersWitoutAuth);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.GET, entity, String.class);

        assertEquals("Invalid response code", 403, response.getStatusCodeValue());
        String expected = "{\"httpStatus\":403,\"messages\":[\"Authorization required\"]}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void createUser_ShouldReturn200AndStoreNewUser() throws Exception {
        BlogUser user = new BlogUser();
        user.setName("Created User");
        user.setEmail("created.user@test.com");
        user.setPassword("Qwerty12");

        HttpEntity<BlogUser> entity = new HttpEntity<BlogUser>(user, headersWithAuth);

        BlogUser storedUser = userRepository.findByEmail("created.user@test.com");

        assertNull(storedUser);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.POST, entity, String.class);

        assertEquals("Invalid response code", 200, response.getStatusCodeValue());

        storedUser = userRepository.findByEmail("created.user@test.com");
        assertNotNull(storedUser);
        assertEquals("Created User", storedUser.getName());
        assertEquals("created.user@test.com", storedUser.getEmail());
        assertEquals("Qwerty12", storedUser.getPassword());
        assertNotNull(storedUser.getId());
    }

    @Test
    public void createUser_ShouldReturn400AndNotStoreNewUser_WhenPasswordNotValidated() throws Exception {
        BlogUser user = new BlogUser();
        user.setName("Created User");
        user.setEmail("created.user@test.com");
        user.setPassword("q");

        HttpEntity<BlogUser> entity = new HttpEntity<BlogUser>(user, headersWithAuth);

        BlogUser storedUser = userRepository.findByEmail("created.user@test.com");

        assertNull(storedUser);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.POST, entity, String.class);

        assertEquals("Invalid response code", 400, response.getStatusCodeValue());

        storedUser = userRepository.findByEmail("created.user@test.com");
        assertNull(storedUser);
    }

    @Test
    public void createUser_ShouldReturn400AndNotStoreNewUser_WhenEmailNotValidated() throws Exception {
        BlogUser user = new BlogUser();
        user.setName("Created User");
        user.setEmail("created.user");
        user.setPassword("Qwerty12");

        HttpEntity<BlogUser> entity = new HttpEntity<BlogUser>(user, headersWithAuth);

        BlogUser storedUser = userRepository.findByEmail("created.user@test.com");

        assertNull(storedUser);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/admin/users/"),
                HttpMethod.POST, entity, String.class);

        assertEquals("Invalid response code", 400, response.getStatusCodeValue());

        storedUser = userRepository.findByEmail("created.user@test.com");
        assertNull(storedUser);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}