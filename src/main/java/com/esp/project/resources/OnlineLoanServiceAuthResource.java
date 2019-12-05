package com.esp.project.resources;

import com.codahale.metrics.annotation.Timed;
import com.esp.project.config.OktaOAuthConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Timed
@Path("/login")
public class OnlineLoanServiceAuthResource {

	private final OktaOAuthConfig config;

	public OnlineLoanServiceAuthResource(OktaOAuthConfig config) {
		this.config = config;
	}

	@GET
	public OktaOAuthConfig getConfig() {
		return config;
	}
}
