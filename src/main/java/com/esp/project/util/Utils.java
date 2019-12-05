package com.esp.project.util;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Utils {

	private static final String ERROR_FIELD               = "error";
	private static final String EXCEPTION_FIELD           = "exception";
	private static final String CAUSE_FIELD               = "cause";

	public static Response exception(final String message, final Exception exception) {
		final ImmutableMap.Builder respBuilder = ImmutableMap
				.builder()
				.put(ERROR_FIELD, message)
				.put(EXCEPTION_FIELD, exception.getMessage());

		if (exception.getCause() != null) {
			respBuilder.put(CAUSE_FIELD, exception.getCause().getMessage());
		}

		return Response.serverError().entity(respBuilder.build()).build();
	}

	public static Response badRequest(final String message, final String badRequestField) {
		final ImmutableMap.Builder respBuilder = ImmutableMap
				.builder()
				.put(ERROR_FIELD, message)
				.put(EXCEPTION_FIELD, badRequestField);
		return Response.notAcceptable(null).entity(respBuilder.build()).build();
	}

	public static Response forbiddenRequest() {
		return Response.status(Response.Status.FORBIDDEN)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.entity("Credentials does not match to access this resource.")
				.build();
	}
}
