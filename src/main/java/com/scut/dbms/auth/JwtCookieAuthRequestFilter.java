package com.scut.dbms.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;

@Priority(Priorities.AUTHENTICATION)
class JwtCookieAuthRequestFilter<P extends JwtCookiePrincipal> extends AuthFilter<String, P> {

    private final String cookieName;

    private JwtCookieAuthRequestFilter(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        Cookie cookie = crc.getCookies().get(cookieName);
        if (null != cookie) {
            String accessToken = cookie.getValue();
            if (accessToken != null && accessToken.length() > 0) {
                try {
                    final Optional<P> subject = authenticator.authenticate(accessToken);
                    if (subject.isPresent()) {
                        crc.setSecurityContext(new JwtCookieSecurityContext(subject.get(), crc.getSecurityContext().isSecure()));
                        return;
                    }
                } catch (AuthenticationException e) {
                    throw new InternalServerErrorException(e);
                }
            }
        }
        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    public static class Builder<P extends JwtCookiePrincipal> extends AuthFilterBuilder<String, P, JwtCookieAuthRequestFilter<P>> {

        private String cookieName;

        public Builder setCookieName(String cookieName) {
            this.cookieName = cookieName;
            return this;
        }

        @Override
        protected  JwtCookieAuthRequestFilter<P> newInstance() {
            return new JwtCookieAuthRequestFilter(Objects.requireNonNull(cookieName, "cookieName is not set"));
        }
    }
}
