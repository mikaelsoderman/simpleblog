package se.soderman.simpleblog.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Locale;

@Component
@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    private final MessageSource messageSource;

    @Autowired
    public ExceptionMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(Exception e) {
        return RestResponseCreator.response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageSource.getMessage("invalid.error", null, Locale.getDefault()), e);
    }
}
