package se.soderman.simpleblog.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.dao.PostRepository;
import se.soderman.simpleblog.domain.BlogUser;
import se.soderman.simpleblog.domain.Post;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class BackendConfiguration {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Autowired
    private BlogUserRepository blogUserRepository;

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void initialDataSetup() {
        BlogUser user = new BlogUser();
        user.setEmail("user@test.com");
        user.setName("Enigio User");
        user.setPassword("12345Qw");
        user = blogUserRepository.save(user);

        Post p1 = new Post();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d = null;
        try {
            d = sdf.parse("21/05/2018 13:40");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p1.setDate(d);
        p1.setAuthor(user);
        p1.setTitle("Lorem ipsum dolor sit amet");
        p1.setBody("Sed venenatis convallis justo, vel fringilla libero. Curabitur in varius lorem, in hendrerit neque. Nunc lobortis pretium justo pretium vulputate. ");
        p1.setImageUrl("https://picsum.photos/600/300/?image=724");

        Post p = new Post();
        Date d2 = null;
        try {
            d2 = sdf.parse("22/05/2018 14:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setDate(d2);
        p.setAuthor(user);
        p.setTitle("Duis vel tristique purus");
        p.setBody("Donec aliquet mauris dui. Nullam accumsan aliquet nisi eget laoreet. Etiam blandit tempor facilisis. Nulla quis hendrerit magna. Donec posuere risus dictum dolor volutpat, vel pharetra nisi aliquet. Nunc condimentum feugiat velit, et finibus nibh accumsan ut. Etiam ac efficitur sem. Sed eget cursus lacus, at tempus magna. ");
        p.setImageUrl("https://picsum.photos/600/300/?image=542");

        postRepository.save(p);
        postRepository.save(p1);
    }
}
