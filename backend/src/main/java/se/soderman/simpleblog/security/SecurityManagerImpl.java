package se.soderman.simpleblog.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.domain.BlogUser;

import java.util.List;

@Component
public class SecurityManagerImpl implements SecurityManager {
    private final BlogUserRepository userRepository;

    @Autowired
    public SecurityManagerImpl(BlogUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAuthorized(String email, String pass, List resources, List actions) {
        BlogUser user = userRepository.findByEmail(email);
        return user != null && user.getEmail().equals(email) && user.getPassword().equals(pass);
    }

}
