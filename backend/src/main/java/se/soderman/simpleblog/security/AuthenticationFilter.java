package se.soderman.simpleblog.security;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import se.soderman.simpleblog.exception.RestResponseCreator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

@Provider
@Component
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {
    private static final String AUTHORIZATION_PROPERTY_NAME = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Context
    private ResourceInfo resourceInfo;

    private final MessageSource messageSource;
    private final SecurityManagerImpl securityManager;

    @Autowired
    public AuthenticationFilter(SecurityManagerImpl securityManager, MessageSource messageSource) {
        this.securityManager = securityManager;
        this.messageSource = messageSource;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(SecuredEndpoint.class)) {

            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            String authorizationPropertyValue = headers.getFirst(AUTHORIZATION_PROPERTY_NAME);
            if (StringUtils.isEmpty(authorizationPropertyValue)) {
                requestContext.abortWith(RestResponseCreator.response(Response.Status.FORBIDDEN.getStatusCode(),
                        messageSource.getMessage("rest.forbidden", null, Locale.getDefault())));
                return;
            }
            String user;
            String pass;
            try {
                String base64Credentials = authorizationPropertyValue.substring(AUTHENTICATION_SCHEME.length()).trim();
                String credentials = new String(Base64.decode(base64Credentials.getBytes()));
                final String[] credentialValues = credentials.split(":",2);

                user = credentialValues[0];
                pass = credentialValues[1];
            } catch (Exception e) {
                requestContext.abortWith(RestResponseCreator.response(Response.Status.BAD_REQUEST.getStatusCode(),
                        messageSource.getMessage("rest.auth.invalid", null, Locale.getDefault())));
                return;
            }
            SecuredEndpoint annotation = method.getAnnotation(SecuredEndpoint.class);
            if (!securityManager.isAuthorized(user, pass, Arrays.asList(annotation.resources()), Arrays.asList(annotation.actions()))) {
                requestContext.abortWith(RestResponseCreator.response(Response.Status.UNAUTHORIZED.getStatusCode(),
                        messageSource.getMessage("rest.auth.failed", null, Locale.getDefault())));
            }
        }
    }
}
