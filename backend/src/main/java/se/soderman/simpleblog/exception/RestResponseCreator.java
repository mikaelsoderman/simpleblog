package se.soderman.simpleblog.exception;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public abstract class RestResponseCreator {
    private static Logger logger = LogManager.getLogger();

    public static Response response(int status, String message) {
        return response(status, message, null);
    }

    public static Response response(int status, List<String> messages, Throwable t) {
        if (t != null) {
            logger.catching(Level.DEBUG, t);
        }
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .setHttpStatus(status).setMessages(messages).build();
        return Response.status(status).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
    }

    public static Response response(int status, String message, Throwable t) {
        if (t != null) {
            logger.catching(Level.DEBUG, t);
        }
        ErrorMessage errorMessage = new ErrorMessage.Builder()
                .setHttpStatus(status).addMessage(message).build();
        return Response.status(status).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
    }

}
