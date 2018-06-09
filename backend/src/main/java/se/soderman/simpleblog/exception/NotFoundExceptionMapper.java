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
public class NotFoundExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<NotFoundException> {

    private final MessageSource messageSource;

    @Autowired
    public NotFoundExceptionMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(NotFoundException e) {
        return RestResponseCreator.response(HttpStatus.NOT_FOUND.value(),
                messageSource.getMessage("invalid.post.unknown", null, Locale.getDefault()), e);
    }
}