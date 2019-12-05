package com.esp.project.models;

public enum LoanType {
	EDUCATION("education"),
	AUTOMOBILE("automobile"),
	MORTGAGE("mortgage");

	private final String loanType;

	LoanType(final String loanType) {
		this.loanType = loanType;
	}

	public String getLoanType() {
		return this.loanType;
	}

	public static LoanType fromString(final String text) throws IllegalArgumentException {
		for (LoanType r : LoanType.values()) {
			if (r.loanType.equalsIgnoreCase(text)) {
				return r;
			}
		}
		throw new IllegalArgumentException("No matching loanType found");
	}
}
