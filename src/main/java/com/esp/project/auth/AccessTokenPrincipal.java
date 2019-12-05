package com.esp.project.auth;

import com.okta.jwt.Jwt;

import java.security.Principal;
import java.util.List;

public class AccessTokenPrincipal implements Principal {

	private final Jwt decodedAccessToken;

	protected AccessTokenPrincipal(final Jwt decodedAccessToken) {
		this.decodedAccessToken = decodedAccessToken;
	}

	@Override
	public String getName() {
		// the 'name' claim in the access token will be the name address
		return (String) decodedAccessToken.getClaims().get("name");
	}

	public String getEmail() {
		// the 'email' claim in the access token will be the email address
		return (String) decodedAccessToken.getClaims().get("email");
	}

	public List<String> getGroups() {
		// the 'groups' claim in the access token will be the groups address
		return (List<String>) decodedAccessToken.getClaims().get("groups");
	}

	public String getUserId() {
		// the 'uid' claim in the access token will be the user id
		return (String) decodedAccessToken.getClaims().get("uid");
	}

}
