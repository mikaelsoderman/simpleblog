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
        user.setEmail("hej@hopp.se");
        user.setName("Kalle Kula");
        user.setPassword("Aa123456");
        user = blogUserRepository.save(user);

        Post p = new Post();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse("21/12/2012");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        p.setDate(d);
        p.setAuthor(user);
        p.setTitle("This is a title");
        p.setBody("THis is the body");

        postRepository.save(p);
    }
}
