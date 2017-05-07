package com.scut.dbms.auth;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Bundle configuration class
 */
public class JwtCookieAuthConfiguration {

    private String secretSeed;

    private boolean secure = false;

    private boolean httpOnly = true;

    @NotEmpty
    private String sessionExpiryVolatile = "PT30m";

    @NotEmpty
    private String sessionExpiryPersistent = "P7d";

    /**
     * The secret seed use to generate the signing key.
     * It can be used to keep the same key value across application reboots.
     * @return the signing key seed
     */
    public String getSecretSeed() {
        return secretSeed;
    }

    /**
     * Indicates if the 'secure' flag must be set on cookies
     * @return if the 'secure' flag must be set on cookies
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * Indicates if the 'secure' flag must be set on cookies
     * @return if the 'secure' flag must be set on cookies
     */
    public boolean isHttpOnly() {
        return httpOnly;
    }

    /**
     * duration of volatile cookies (in ISO 8601 format)
     * @return the duration of volatile cookies
     */
    public String getSessionExpiryVolatile() {
        return sessionExpiryVolatile;
    }

    /**
     * duration of persistent cookies (in ISO 8601 format)
     * @return the duration of persistent cookies
     */
    public String getSessionExpiryPersistent() {
        return sessionExpiryPersistent;
    }
}
