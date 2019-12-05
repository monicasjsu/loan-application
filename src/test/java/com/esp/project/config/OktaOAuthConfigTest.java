package com.esp.project.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OktaOAuthConfigTest {
	private String baseUrl = "testbaseUrl";
	private String clientId = "testClientId";
	private String clientSecret = "testClientSecret";
	private String issuer = "testIssuer";
	private String audience = "testAudience";
	private String rolesClaim = "testRoleClaim";

	private OktaOAuthConfig oktaOAuthConfig;

	@Before
	public void setUp() {
		oktaOAuthConfig = new OktaOAuthConfig(baseUrl, clientId, clientSecret, issuer, audience, rolesClaim);
	}

	@Test
	public void testGetters() {
		assertEquals(baseUrl, oktaOAuthConfig.getBaseUrl());
		assertEquals(clientId, oktaOAuthConfig.getClientId());
		assertEquals(clientSecret, oktaOAuthConfig.getClientSecret());
		assertEquals(issuer, oktaOAuthConfig.getIssuer());
		assertEquals(audience, oktaOAuthConfig.getAudience());
		assertEquals(rolesClaim, oktaOAuthConfig.getRolesClaim());
	}
}
