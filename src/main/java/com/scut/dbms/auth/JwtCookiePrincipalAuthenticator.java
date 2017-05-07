package com.scut.dbms.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.security.Key;
import java.util.Optional;
import java.util.function.Function;

class JwtCookiePrincipalAuthenticator<P extends JwtCookiePrincipal> implements Authenticator<String, P> {

    private final Key key;
    private final Function<Claims, P> deserializer;

    public JwtCookiePrincipalAuthenticator(Key key, Function<Claims, P> deserializer) {
        this.key = key;
        this.deserializer = deserializer;
    }

    @Override
    public Optional<P> authenticate(String credentials) throws AuthenticationException {
        try {
            return Optional.of(deserializer.apply(Jwts.parser().setSigningKey(key).parseClaimsJws(credentials).getBody()));
        } catch (ExpiredJwtException | SignatureException e) {
            return Optional.empty();
        }
    }

}
