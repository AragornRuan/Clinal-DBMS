package com.scut.dbms.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.scut.dbms.auth.DefaultJwtCookiePrincipal;
import com.scut.dbms.auth.JwtCookiePrincipal;
import com.scut.dbms.db.InvitationDAO;
import com.scut.dbms.db.UsersDAO;

import io.dropwizard.auth.Auth;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AuthResources {
	
	private UsersDAO usersDAO;
	private InvitationDAO invitationDAO;
	
	public AuthResources(UsersDAO usersDAO, InvitationDAO invitationDAO) {
		this.usersDAO = usersDAO;
		this.invitationDAO = invitationDAO;
	}
	
	@POST
	@Path("/login")
	public DefaultJwtCookiePrincipal login(@Context ContainerRequestContext requestContext, String name){
	    DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(name);
	    principal.addInContext(requestContext);
	    return principal;
	}
	
	@GET
	@Path("/logout")
	public String logout(@Context ContainerRequestContext requestContext){
		JwtCookiePrincipal.removeFromContext(requestContext);
		return "Success";
	}
	
	@GET
	@Path("hello")
	public String getHello(@Auth DefaultJwtCookiePrincipal principal) {
		return "hello, world";
	}

}
