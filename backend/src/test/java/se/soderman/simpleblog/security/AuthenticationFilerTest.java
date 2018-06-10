package se.soderman.simpleblog.security;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import se.soderman.simpleblog.security.AuthenticationFilter;
import se.soderman.simpleblog.security.SecurityManagerImpl;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilerTest {

    private AuthenticationFilter authenticationFilter;

    @Mock
    private MessageSource messageSource;
    @Mock
    private SecurityManagerImpl securityManager;
    @Mock
    private ResourceInfo resourceInfo;
    @Mock
    private Method method;
    @Mock
    private ContainerRequestContext requestContextMock;
    @Mock
    private SecuredEndpoint securedEndpointMock;

    private MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();


    @Before
    public void setUp() throws Exception {
        when(method.isAnnotationPresent(any())).thenReturn(true);
        when(method.getAnnotation(SecuredEndpoint.class)).thenReturn(securedEndpointMock);
        when(resourceInfo.getResourceMethod()).thenReturn(method);
        when(requestContextMock.getHeaders()).thenReturn(headers);
        when(securedEndpointMock.resources()).thenReturn(new String[]{});
        when(securedEndpointMock.actions()).thenReturn(new String[]{});

        authenticationFilter = new AuthenticationFilter(securityManager, messageSource, resourceInfo);
    }

    @Test
    public void filter_DoesNotAbort_IfMethodIsNotAnnotated() throws Exception {
        when(method.isAnnotationPresent(any())).thenReturn(false);
        authenticationFilter.filter(requestContextMock);
        assertNotAborted();
    }

    @Test
    public void filter_DoesAbortWith403_IfAuthorizationHeaderMissing() throws Exception {
        authenticationFilter.filter(requestContextMock);
        assertAborted(403);
    }

    @Test
    public void filter_DoesAbortWith400_IfAuthorizationHeaderHasWrongFormat() throws Exception {
        headers.add("Authorization", "testing");
        authenticationFilter.filter(requestContextMock);
        assertAborted(400);
    }

    @Test
    public void filter_DoesAbortWith401_IfNotAuthorized() throws Exception {
        String creds = new String(Base64.encode("TheUser:ThePass".getBytes()));
        headers.add("Authorization", "Basic"+creds);
        when(securityManager.isAuthorized(any(), any(), any(), any())).thenReturn(false);

        authenticationFilter.filter(requestContextMock);
        assertAborted(401);
    }

    @Test
    public void filter_DoesNotAbort_IfAuthorized() throws Exception {
        String creds = new String(Base64.encode("TheUser:ThePass".getBytes()));
        headers.add("Authorization", "Basic"+creds);
        when(securityManager.isAuthorized(any(), any(), any(), any())).thenReturn(true);

        authenticationFilter.filter(requestContextMock);
        assertNotAborted();
    }

    private void assertNotAborted() {
        verify(requestContextMock, times(0)).abortWith(any());
    }

    private void assertAborted(int responseCode) {
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        verify(requestContextMock, times(1)).abortWith(captor.capture());

        assertEquals("Wrong response code", responseCode, captor.getValue().getStatus());
    }
}
