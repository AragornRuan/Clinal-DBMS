package com.scut.dbms.auth;

import java.security.Principal;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * A principal persisted in JWT cookies
 */
public interface JwtCookiePrincipal extends Principal{
    
    /**
     * Indicates if the cookie will be persistent (aka 'remember me')
     * @return if the cookie must be persistent
     */
   boolean isPersistent(); 
   
   /**
    * Indicates if this principal has the given role
    * @param role the role
    * @return true if the principal is in the given role, false otherwise
    */
   boolean isInRole(String role);
   
   /**
    * Add this principal in the request context.
    * It will serialized in a JWT cookie and can be reused in subsequent queries
    * @param context the request context
    */
   default void addInContext(ContainerRequestContext context){
        context.setSecurityContext(new JwtCookieSecurityContext(this, context.getSecurityContext().isSecure()));
    }
   
   public static void removeFromContext(ContainerRequestContext context){
       context.setSecurityContext(new JwtCookieSecurityContext(null, context.getSecurityContext().isSecure()));
   }
   
}
