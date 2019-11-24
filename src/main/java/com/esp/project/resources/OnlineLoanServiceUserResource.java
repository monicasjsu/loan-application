package com.esp.project.resources;


import com.codahale.metrics.annotation.Timed;
import com.esp.project.auth.AccessTokenPrincipal;
import com.esp.project.client.ApplicationClient;
import com.esp.project.models.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.esp.project.util.Utils.exception;
import static com.google.common.collect.ImmutableSetMultimap.of;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Timed
@Path("/users")
public class OnlineLoanServiceUserResource {

	private final ApplicationClient applicationClient;

	public OnlineLoanServiceUserResource(final ApplicationClient applicationClient) {
		this.applicationClient = applicationClient;
	}

	@POST
	@Path("/new-user")
	public Response signUpUser(@Auth final AccessTokenPrincipal tokenPrincipal, final User userData) {
		try {
			userData.setUserId(tokenPrincipal.getUserId());
			userData.setEmail(tokenPrincipal.getEmail());
			applicationClient.newUser(userData);
			return Response.ok().build();
		}
		catch (Exception ex) {
			return exception("NewUser exception", ex);
		}
	}

	@GET
	@Path("/user-profile")
	public Response hasUserProfile(@Auth final AccessTokenPrincipal tokenPrincipal, final String email ) {
		final User userDataInfo = applicationClient.selectUser(tokenPrincipal.getEmail());
		return Response.ok(userDataInfo).build();
	}

	@POST
	@Path("/update-user")
	public Response updateUserQuery(@Auth final AccessTokenPrincipal tokenPrincipal, final User userData) {
		userData.setUserId(tokenPrincipal.getUserId());
		userData.setEmail(tokenPrincipal.getEmail());
		applicationClient.userUpdate(userData);
		return Response.ok().build();
	}
}
