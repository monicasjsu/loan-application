package com.esp.project;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.junit.Assert.assertEquals;

import com.esp.project.config.AwsConfig;
import com.esp.project.config.EnvironmentConfig;
import com.esp.project.config.OktaOAuthConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import io.dropwizard.db.DataSourceFactory;
import org.junit.Test;

public class OnlineLoanServiceConfigurationTest {

	private final String awsAccessKey = "testAccessKey";
	private final String awsSecretKey = "testSecretKey";
	private String baseUrl = "testBaseUrl";
	private String clientId = "testClientId";
	private String clientSecret = "testClientSecret";
	private String issuer = "testIssuer";
	private String audience = "testAudience";
	private String rolesClaim = "testRolesClaim";

	private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

	private EnvironmentConfig environmentConfig = new EnvironmentConfig("testing");
	private AwsConfig awsConfig = new AwsConfig(awsAccessKey, awsSecretKey);
	private OktaOAuthConfig oktaOAuthConfig = new OktaOAuthConfig(baseUrl, clientId, clientSecret,
			issuer, audience, rolesClaim);

	@Test
	public void testConfigDeserialization() throws Exception {
		mapper.registerModules(new GuavaModule());
		OnlineLoanServiceConfiguration configuration = new OnlineLoanServiceConfiguration(environmentConfig,
				new DataSourceFactory(), oktaOAuthConfig, awsConfig);
		OnlineLoanServiceConfiguration loadedConfig = mapper.readValue(fixture("fixtures/testConfig.yml"),
				OnlineLoanServiceConfiguration.class);

		assertEquals(configuration.getAuthConfig(), loadedConfig.getAuthConfig());
		assertEquals(configuration.getAwsConfig(), loadedConfig.getAwsConfig());
		assertEquals(configuration.getEnvironment(), loadedConfig.getEnvironment());
	}
}
