package se.soderman.simpleblog.security;

import java.util.List;

public interface SecurityManager {
    boolean isAuthorized(String user, String pass, List resources, List actions);
}
