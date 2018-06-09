package se.soderman.simpleblog.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Locale;

@Component
@Provider
public class DataIntegrityViolationExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<DataIntegrityViolationException> {

    private final MessageSource messageSource;

    @Autowired
    public DataIntegrityViolationExceptionMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(DataIntegrityViolationException e) {
        return RestResponseCreator.response(HttpStatus.CONFLICT.value(),
                messageSource.getMessage("invalid.email.alreadyregistered", null, Locale.getDefault()),
                e);
    }
}
