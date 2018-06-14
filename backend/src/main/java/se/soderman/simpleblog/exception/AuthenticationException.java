package se.soderman.simpleblog.exception;

public class AuthenticationException extends Exception {
    private String user;

    public AuthenticationException(String message, String user) {
        super(message);
        this.user = user;
    }

    public String getResource() {
        return user;
    }

    @Override
    public String toString() {
        return "AuthenticationException{" +
                "resource='" + user + '\'' +
                '}';
    }
}
