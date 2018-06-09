package se.soderman.simpleblog.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Component
@Provider
public class TransactionSystemExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<TransactionSystemException> {

    private final MessageSource messageSource;

    @Autowired
    public TransactionSystemExceptionMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Response toResponse(TransactionSystemException e) {

        if (e.getCause() instanceof RollbackException) {
            if (e.getCause().getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) e.getCause().getCause();
                List<String> messages = cve.getConstraintViolations().stream().map(violation -> messageSource.getMessage(violation.getMessage(), null, Locale.getDefault())).collect( Collectors.toList() );
                return RestResponseCreator.response(HttpStatus.BAD_REQUEST.value(), messages, e.getCause().getCause());
            }
        }

        return RestResponseCreator.response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageSource.getMessage("invalid.transaction", null, Locale.getDefault()),
                e);
    }
}