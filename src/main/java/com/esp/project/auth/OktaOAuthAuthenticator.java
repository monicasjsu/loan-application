package com.esp.project.auth;

import com.okta.jwt.JoseException;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerifier;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class OktaOAuthAuthenticator implements Authenticator<String, AccessTokenPrincipal> {

	final Logger log = LoggerFactory.getLogger(OktaOAuthAuthenticator.class);

	private final JwtVerifier jwtVerifier;

	public OktaOAuthAuthenticator(JwtVerifier jwtVerifier) {
		this.jwtVerifier = jwtVerifier;
	}

	@Override
	public Optional<AccessTokenPrincipal> authenticate(final String accessToken) {
		try {
			final Jwt jwt = jwtVerifier.decodeAccessToken(accessToken);
			return Optional.of(new AccessTokenPrincipal(jwt));
		} catch (JoseException e) {
			log.error("Exception authenticating", e);
			return Optional.empty();
		}
	}
}
