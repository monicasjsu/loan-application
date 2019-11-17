package com.esp.project.models;

import javax.annotation.security.RolesAllowed;

public enum UserRole {
	APPROVER("loanAppAdmin"),
	APPLICANT("applicant"),
	UNKNOWN("unknown");

	private final String userRole;

	UserRole(final String userRole) {

		this.userRole = userRole;
	}

	public String getUserRole() {

		return this.userRole;
	}

	public static UserRole fromString(final String text) throws IllegalArgumentException {
		for (UserRole r : UserRole.values()) {
			if (r.userRole.equalsIgnoreCase(text)) {
				return r;
			}
		}
		return UNKNOWN;
	}
}
