package com.esp.project.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class OnlineLoanServiceHomeResourceTest {

	private final String homePath = "/";

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new OnlineLoanServiceHomeResource())
			.build();

	@Test
	public void testHeartbeat() {
		final Response apiResponse = resources.target(homePath).path("/health")
				.request().get();
		final String body = apiResponse.readEntity(String.class);
		assertThat(apiResponse.getStatus()).isEqualTo(200);
		assertThat(body).isEqualTo("{\"isAlive\":[true]}");
	}
}
