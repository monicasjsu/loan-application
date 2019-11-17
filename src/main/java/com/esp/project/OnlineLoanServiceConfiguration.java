package com.esp.project;

import com.esp.project.config.AwsConfig;
import com.esp.project.config.EnvironmentConfig;
import com.esp.project.config.OktaOAuthConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class OnlineLoanServiceConfiguration extends Configuration {

	private final EnvironmentConfig environment;
	@Valid @NotNull private final DataSourceFactory database;
	@Valid @NotNull private final OktaOAuthConfig authConfig;
	@Valid @NotNull private final AwsConfig awsConfig;

	public OnlineLoanServiceConfiguration(@JsonProperty("environment") final EnvironmentConfig environment,
	                                      @JsonProperty("database") final DataSourceFactory database,
	                                      @JsonProperty("oktaOAuth") final OktaOAuthConfig authConfig,
	                                      @JsonProperty("aws") final AwsConfig awsConfig) {
		super();
		this.environment = environment;
		this.database = database;
		this.authConfig = authConfig;
		this.awsConfig = awsConfig;
	}

	public EnvironmentConfig getEnvironment() {
		return environment;
	}

	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	public OktaOAuthConfig getAuthConfig() {
		return authConfig;
	}

	public AwsConfig getAwsConfig() {
		return awsConfig;
	}
}
