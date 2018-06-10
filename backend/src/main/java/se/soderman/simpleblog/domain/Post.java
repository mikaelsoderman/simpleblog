package se.soderman.simpleblog.domain;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.util.Date;

@Entity
@Indexed
@SequenceGenerator(name="seq", initialValue=1, allocationSize=10)
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Column(name = "id", updatable = false, nullable = false)
    protected Integer id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    @Field(termVector = TermVector.YES)
    private String title;

    @Column(nullable = false)
    @Field(termVector = TermVector.YES)
    private String body;

    @OneToOne
    private BlogUser author;

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BlogUser getAuthor() {
        return author;
    }

    public void setAuthor(BlogUser author) {
        this.author = author;
    }
}
