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
    public void init() {
        BlogUser user = new BlogUser();
        user.setEmail("admin@admin.se");
        user.setName("Kalle Kula");
        user.setPassword("Aa123456");
        user = blogUserRepository.save(user);

        Post p1 = new Post();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse("21/12/2012");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p1.setDate(d);
        p1.setAuthor(user);
        p1.setTitle("This is a title");
        p1.setBody("This is the body");
        p1.setImageUrl("https://picsum.photos/600/300/?image=724");

        Post p = new Post();
        Date d2 = null;
        try {
            d2 = sdf.parse("23/12/2012");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setDate(d2);
        p.setAuthor(user);
        p.setTitle("This is another title");
        p.setBody("This is another body");
        p.setImageUrl("https://picsum.photos/600/300/?image=542");

        postRepository.save(p);
        postRepository.save(p1);
    }
}
