package com.esp.project.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AwsConfig {

	private final String accessKey;
	private final String secretKey;

	@JsonCreator
	public AwsConfig(@JsonProperty("accessKey") final String accessKey,
	                 @JsonProperty("secretKey") final String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}
}
