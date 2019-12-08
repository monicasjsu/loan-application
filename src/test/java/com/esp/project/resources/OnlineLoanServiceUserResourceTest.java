package com.esp.project.resources;

import com.esp.project.auth.AccessTokenPrincipal;
import com.esp.project.auth.OktaOAuthAuthenticator;
import com.esp.project.auth.RoleAuthorization;
import com.esp.project.client.ApplicationClient;
import com.esp.project.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.nimbusds.jwt.JWT;
import com.okta.jwt.JoseException;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerifier;
import com.okta.jwt.impl.DefaultJwt;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class OnlineLoanServiceUserResourceTest {

	private final String userPath = "/users";

	private String firstName = "testFirstName";
	private String lastName = "testLastName";
	private String userId = "testUserId";
	private String email = "testEmail";

	private static final ApplicationClient APPLICATION_CLIENT = mock(ApplicationClient.class);

	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	private static final JwtVerifier JWT_VERIFIER = mock(JwtVerifier.class);

	private final Jwt jwt = new DefaultJwt("TOKEN", Instant.EPOCH, Instant.EPOCH,
			ImmutableMap.of("name", "testName",
					"email", email,
					"groups", Arrays.asList("Everyone", "loanAppAdmin"),
					"uid", userId));

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.setTestContainerFactory(new GrizzlyWebTestContainerFactory())
			.addProvider(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<AccessTokenPrincipal>()
					.setAuthenticator(new OktaOAuthAuthenticator(JWT_VERIFIER))
					.setAuthorizer(new RoleAuthorization())
					.setRealm("SUPER SECRET STUFF")
					.setPrefix("Bearer")
					.buildAuthFilter()))
			.addProvider(RolesAllowedDynamicFeature.class)
			.addProvider(new AuthValueFactoryProvider.Binder<>(AccessTokenPrincipal.class))
			.addResource(new OnlineLoanServiceUserResource(APPLICATION_CLIENT))
			.build();

	@Test
	public void testSignUpUser() throws JoseException {
		doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
		doNothing().when(APPLICATION_CLIENT).newUser(any(User.class));
		User user = getTestUser();
		final Response response = resources
				.target(userPath)
				.path("/new-user")
				.request()
				.header("Authorization", "Bearer TOKEN")
				.post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testUpdateUserQuery() throws JoseException {
		doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
		User user = getTestUser();
		doReturn(1).when(APPLICATION_CLIENT).updateUser(any(User.class));
		final Response response = resources
				.target(userPath)
				.path("/update-user")
				.request()
				.header("Authorization", "Bearer TOKEN")
				.post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testHasUserProfile() throws JoseException, IOException {
		User user = getTestUser();
		doReturn(jwt).when(JWT_VERIFIER).decodeAccessToken(anyString());
		doReturn(user).when(APPLICATION_CLIENT).getUser(anyString());
		final Response response = resources
				.target(userPath)
				.path("/user-profile")
				.request()
				.header("Authorization", "Bearer TOKEN")
				.get();
		assertEquals(200, response.getStatus());
		String body = response.readEntity(String.class);
		User responseUser = MAPPER.readValue(body, User.class);
		assertEquals(user, responseUser);
	}

	private User getTestUser() {
		return new User.Builder()
				.withUserId(userId)
				.withFirstName(firstName)
				.withLastName(lastName)
				.withUserRole(UserRole.APPLICANT)
				.withAddress("testAdd")
				.withEmail(email)
				.withPhone("testPhone")
				.build();
	}
}
