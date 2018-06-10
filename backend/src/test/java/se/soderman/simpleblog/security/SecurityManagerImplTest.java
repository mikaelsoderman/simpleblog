package se.soderman.simpleblog.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import se.soderman.simpleblog.dao.BlogUserRepository;
import se.soderman.simpleblog.domain.BlogUser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityManagerImplTest {

    @InjectMocks
    SecurityManagerImpl securityManager;

    @Mock
    BlogUserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        BlogUser testUser = new BlogUser();
        testUser.setId(1234);
        testUser.setName("My Test");
        testUser.setEmail("test@test.com");
        testUser.setPassword("098765Qw");
        when(userRepository.findByEmail("test@test.com")).thenReturn(testUser);
    }

    @Test
    public void isAuthorized_ShouldFail_IfPasswordIsIncorrect() throws Exception {
        boolean isAuth = securityManager.isAuthorized("test@test.com", "wrongpass", null, null);
        assertAuthFailed(isAuth);
    }

    @Test
    public void isAuthorized_ShouldFail_IfUserIsUnknown() throws Exception {
        boolean isAuth = securityManager.isAuthorized("no.such@user.com", "098765Qw", null, null);
        assertAuthFailed(isAuth);
    }

    @Test
    public void isAuthorized_ShouldNotFail_IfUserIsCorrect() throws Exception {
        boolean isAuth = securityManager.isAuthorized("test@test.com", "098765Qw", null, null);
        assertAuthSucceeded(isAuth);
    }

    private void assertAuthFailed(boolean isAuth) {
        assertFalse("Expected authentication to fail but it was successful", isAuth);
    }

    private void assertAuthSucceeded(boolean isAuth) {
        assertTrue("Authentication failed unexpectedly", isAuth);
    }
}
