package com.esp.project.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AwsConfig awsConfig = (AwsConfig) o;
		return Objects.equal(accessKey, awsConfig.accessKey) &&
				Objects.equal(secretKey, awsConfig.secretKey);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(accessKey, secretKey);
	}
}
