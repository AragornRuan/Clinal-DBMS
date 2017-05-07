package com.scut.dbms.auth;

import java.security.Principal;
import java.util.Optional;
import javax.ws.rs.core.SecurityContext;

/**
 * Security context set after a JWT cookie authentication
 */
class JwtCookieSecurityContext implements SecurityContext{

    private final JwtCookiePrincipal subject;
    private final boolean secure;

    public JwtCookieSecurityContext(JwtCookiePrincipal subject, boolean secure) {
        this.subject = subject;
        this.secure = secure;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return subject;
    }

    @Override
    public boolean isUserInRole(String role) {
        return Optional.ofNullable(subject)
                .map(s -> s.isInRole(role))
                .orElse(false);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "JWT_COOKIE";
    }
    
}
