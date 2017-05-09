package com.scut.dbms.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scut.dbms.api.ResponseMessage;
import com.scut.dbms.auth.DefaultJwtCookiePrincipal;
import com.scut.dbms.auth.JwtCookiePrincipal;
import com.scut.dbms.core.Invitation;
import com.scut.dbms.core.Users;
import com.scut.dbms.db.InvitationDAO;
import com.scut.dbms.db.UsersDAO;
import com.scut.dbms.error.ErrorCode;

import io.dropwizard.auth.Auth;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AuthResources {

	private UsersDAO usersDAO;
	private InvitationDAO invitationDAO;
	private String loginUsername;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthResources.class);

	public AuthResources(UsersDAO usersDAO, InvitationDAO invitationDAO) {
		this.usersDAO = usersDAO;
		this.invitationDAO = invitationDAO;
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public DefaultJwtCookiePrincipal login(@Context ContainerRequestContext requestContext,
			@FormParam("username") String username, @FormParam("password") String password) {

		Users users = usersDAO.findByUsername(username);
		if (users == null) {
			throw new WebApplicationException("用户名不存在！");
		}
		if (!users.getPassword().equals(password)) {
			LOGGER.info("Password is: {}.", password);
			throw new WebApplicationException("密码错误！");
		}

		DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(username);
		principal.addInContext(requestContext);
		setLoginUsername(username);
		return principal;
	}

	@GET
	@Path("/logout")
	public ResponseMessage logout(@Context ContainerRequestContext requestContext) {
		JwtCookiePrincipal.removeFromContext(requestContext);
		return new ResponseMessage(ErrorCode.SUCCESS, "Logout success.");
	}
	
	@POST
	@Path("/register")
	public ResponseMessage register(@NotNull @Valid Users users, @QueryParam("code") String code) {
		Invitation invitation = invitationDAO.findByCode(code);
		if (invitation == null) {
			throw new WebServiceException("邀请码错误！");
		}
		usersDAO.insert(users);
		return new ResponseMessage(ErrorCode.SUCCESS, "Register successfully");
	}

	@GET
	@Path("/test")
	public ResponseMessage getHello(@Auth DefaultJwtCookiePrincipal principal) {
		return new ResponseMessage(ErrorCode.SUCCESS, getLoginUsername());
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

}
