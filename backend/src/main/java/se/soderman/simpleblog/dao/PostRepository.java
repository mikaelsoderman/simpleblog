package se.soderman.simpleblog.dao;

import org.springframework.data.repository.CrudRepository;
import se.soderman.simpleblog.domain.Post;

import java.util.Date;

public interface PostRepository extends CrudRepository<Post, Integer> {
    public Iterable<Post> findByDateBetween(Date start, Date end);
    public Iterable<Post> findAllByOrderByDateDesc();
}
