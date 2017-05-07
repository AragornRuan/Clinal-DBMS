package com.scut.dbms.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

class JwtCookieAuthResponseFilter<P extends JwtCookiePrincipal> implements ContainerResponseFilter {

    private static final String COOKIE_TEMPLATE = "=%s; Path=/";
    private static final String SECURE_FLAG="; Secure";
    private static final String HTTP_ONLY_FLAG="; HttpOnly";
    private static final String DELETE_COOKIE_TEMPLATE = "=; Path=/; expires=Thu, 01-Jan-70 00:00:00 GMT";

    private final Class<P> principalType;
    private final Function<P, Claims> serializer;
    private final String cookieName;
    private final String sessionCookieFormat;
    private final String persistentCookieFormat;
    private final String deleteCookie;

    private final Key signingKey;
    private final int volatileSessionDuration; //in seconds
    private final int persistentSessionDuration;

    public JwtCookieAuthResponseFilter(
            Class<P> principalType,
            Function<P, Claims> serializer,
            String cookieName,
            boolean secure,
            boolean httpOnly,
            Key signingKey,
            int volatileSessionDuration,
            int persistentSessionDuration) {

        this.principalType = principalType;
        this.serializer = serializer;
        this.cookieName = cookieName;
        StringBuilder cookieFormatBuilder = new StringBuilder(cookieName).append(COOKIE_TEMPLATE);
        if(secure){
            cookieFormatBuilder.append(SECURE_FLAG);
        }
        if(httpOnly){
            cookieFormatBuilder.append(HTTP_ONLY_FLAG);
        }
        this.sessionCookieFormat = cookieFormatBuilder.toString();
        this.persistentCookieFormat = sessionCookieFormat + "; Max-Age=%d;";
        this.deleteCookie = cookieName + DELETE_COOKIE_TEMPLATE;
        this.signingKey = signingKey;
        this.volatileSessionDuration = volatileSessionDuration;
        this.persistentSessionDuration = persistentSessionDuration;
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        Principal principal = request.getSecurityContext().getUserPrincipal();
        if (request.getSecurityContext() instanceof JwtCookieSecurityContext) {
            if (principalType.isInstance(principal)) {
                if (request.getProperty(DontRefreshSessionFilter.DONT_REFRESH_SESSION_PROPERTY) != Boolean.TRUE) {
                    P cookiePrincipal = (P) principal;
                    String cookie = cookiePrincipal.isPersistent()
                            ? String.format(persistentCookieFormat, getJwt(cookiePrincipal, persistentSessionDuration), persistentSessionDuration)
                            : String.format(sessionCookieFormat, getJwt(cookiePrincipal, volatileSessionDuration));

                    response.getHeaders().add("Set-Cookie", cookie);
                }
            } else if (request.getCookies().containsKey(cookieName)) {
                //the principal has been unset during the response, delete the cookie
                response.getHeaders().add("Set-Cookie", deleteCookie);
            }
        }
    }

    private String getJwt(P subject, int expiresIn) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setClaims(serializer.apply(subject))
                .setExpiration(Date.from(Instant.now().plus(expiresIn, ChronoUnit.SECONDS)))
                .compact();
    }

}
