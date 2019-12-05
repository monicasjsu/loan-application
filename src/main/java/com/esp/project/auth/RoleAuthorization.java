package com.esp.project.auth;

import com.esp.project.models.UserRole;
import io.dropwizard.auth.Authorizer;

public class RoleAuthorization implements Authorizer<AccessTokenPrincipal> {
	@Override
	public boolean authorize(final AccessTokenPrincipal principal, final String role) {
		try {
			for (String principalRole : principal.getGroups()) {
				if (UserRole.fromString(principalRole) == UserRole.APPROVER) {
					return true;
				}
			}
			return false;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}
