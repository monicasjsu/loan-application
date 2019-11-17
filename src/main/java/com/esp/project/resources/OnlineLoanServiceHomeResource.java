package com.esp.project.resources;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.collect.ImmutableSetMultimap.of;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
@PermitAll
public class OnlineLoanServiceHomeResource {

	@GET
	@Path("/health")
	public Response getHealth() {
		return Response.ok(of("isAlive", true)).build();
	}

}
