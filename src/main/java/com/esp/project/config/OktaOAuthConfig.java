package com.esp.project.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OktaOAuthConfig {

	private String baseUrl;
	private String clientId;
	private String clientSecret;
	private String issuer;
	private String audience;
	private String rolesClaim;

	@JsonCreator
	public OktaOAuthConfig(@JsonProperty("name") final String baseUrl,
	                       @JsonProperty("clientId") final String clientId,
	                       @JsonProperty("clientSecret") final String clientSecret,
	                       @JsonProperty("issuer") final String issuer,
	                       @JsonProperty("audience") final String audience,
	                       @JsonProperty("rolesClaim") final String rolesClaim) {
		this.baseUrl = baseUrl;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.issuer = issuer;
		this.audience = audience;
		this.rolesClaim = rolesClaim;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getRolesClaim() {
		return rolesClaim;
	}
}
