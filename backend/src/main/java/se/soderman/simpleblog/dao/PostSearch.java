package se.soderman.simpleblog.dao;

import org.apache.catalina.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.soderman.simpleblog.domain.Post;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class PostSearch {

    private final EntityManager entityManager;

    @Autowired
    public PostSearch(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List search(String text) {

        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Post.class)
                .get();

        org.apache.lucene.search.Query query =
                queryBuilder
                    .keyword()
                    .wildcard()
                    .onFields("title", "body")
                    .matching("*" + text + "*")
                    .createQuery();

        Query keywordQuery = queryBuilder
                .keyword()
                .onField("body")
                .matching(text)
                .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(keywordQuery, Post.class);

        return jpaQuery.getResultList();
    }

}