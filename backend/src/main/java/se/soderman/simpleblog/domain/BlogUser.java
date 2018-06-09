package se.soderman.simpleblog.domain;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@SequenceGenerator(name="seq", initialValue=1, allocationSize=10)
public class BlogUser {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Column(name = "id", updatable = false, nullable = false)
    protected Integer id;

    @Column(unique = true)
    @Pattern(regexp=EMAIL_PATTERN,
            message="invalid.email")
    protected String email;

    @Column(nullable = false)
    @Pattern(regexp=PASSWORD_PATTERN, message = "invalid.password")
    protected String password;

    protected String name;


    public BlogUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
