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
public class AuthenticationExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<AuthenticationException> {

    private final MessageSource messageSource;

    @Autowired
    public AuthenticationExceptionMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(AuthenticationException e) {
        return RestResponseCreator.response(HttpStatus.UNAUTHORIZED.value(),
                messageSource.getMessage("rest.auth.failed", null, Locale.getDefault()), e);
    }
}