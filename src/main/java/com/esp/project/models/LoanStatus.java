package com.esp.project.models;

public enum LoanStatus {
	NEW("new"),
	IN_PROGRESS("in_progress"),
	APPROVED("approved"),
	DECLINED("declined");

	private final String loanStatus;

	LoanStatus(final String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public String getLoanStatus() {
		return this.loanStatus;
	}

	public static LoanStatus fromString(final String text) throws IllegalArgumentException {
		for (LoanStatus r : LoanStatus.values()) {
			if (r.loanStatus.equalsIgnoreCase(text)) {
				return r;
			}
		}
		throw new IllegalArgumentException("No matching loan status found");
	}
}
