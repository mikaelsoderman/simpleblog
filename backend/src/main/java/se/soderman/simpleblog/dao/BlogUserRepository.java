package se.soderman.simpleblog.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.soderman.simpleblog.domain.BlogUser;

@Repository
public interface BlogUserRepository extends CrudRepository<BlogUser, Integer> {
    public BlogUser findByEmail(String email);
}
