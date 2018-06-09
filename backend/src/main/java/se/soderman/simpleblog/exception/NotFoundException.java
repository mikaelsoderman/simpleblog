package se.soderman.simpleblog.exception;

public class NotFoundException extends Exception {
    private String resource;

    public NotFoundException(String message, String resource) {
        super(message);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "NotFoundException{" +
                "resource='" + resource + '\'' +
                '}';
    }
}
