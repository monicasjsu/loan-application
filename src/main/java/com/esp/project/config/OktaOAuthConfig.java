package com.esp.project.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OktaOAuthConfig that = (OktaOAuthConfig) o;
		return Objects.equal(baseUrl, that.baseUrl) &&
				Objects.equal(clientId, that.clientId) &&
				Objects.equal(clientSecret, that.clientSecret) &&
				Objects.equal(issuer, that.issuer) &&
				Objects.equal(audience, that.audience) &&
				Objects.equal(rolesClaim, that.rolesClaim);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(baseUrl, clientId, clientSecret, issuer, audience, rolesClaim);
	}
}
