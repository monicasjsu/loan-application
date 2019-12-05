package com.esp.project.resources;

import com.esp.project.config.OktaOAuthConfig;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OnlineLoanServiceAuthResourceTest {

	private String baseUrl = "testBaseUrl";
	private String clientId = "testClientId";
	private String clientSecret = "testClientSecret";
	private String issuer = "testIssuer";
	private String audience = "testAudience";
	private String rolesClaim = "testRolesClaim";

	private final static OktaOAuthConfig OKTA_O_AUTH_CONFIG = mock(OktaOAuthConfig.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new OnlineLoanServiceAuthResource(OKTA_O_AUTH_CONFIG))
			.build();

	@Before
	public void setUp() {
		doReturn(baseUrl).when(OKTA_O_AUTH_CONFIG).getBaseUrl();
		doReturn(clientId).when(OKTA_O_AUTH_CONFIG).getClientId();
		doReturn(clientSecret).when(OKTA_O_AUTH_CONFIG).getClientSecret();
		doReturn(issuer).when(OKTA_O_AUTH_CONFIG).getIssuer();
		doReturn(audience).when(OKTA_O_AUTH_CONFIG).getAudience();
		doReturn(rolesClaim).when(OKTA_O_AUTH_CONFIG).getRolesClaim();
	}

	@Test
	public void testGetOAuthConfig() {
		OktaOAuthConfig oktaOAuthConfig = new OktaOAuthConfig(baseUrl, clientId, clientSecret,
				issuer, audience, rolesClaim);
		assertThat(resources.target("/login").request().get(OktaOAuthConfig.class))
				.isEqualTo(oktaOAuthConfig);
	}
}
