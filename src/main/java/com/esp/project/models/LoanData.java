package com.esp.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanData {
	@JsonProperty("loanId") private long loanId;
	@JsonProperty("userId") private String userId;
	@JsonProperty("firstName") private String firstName;
	@JsonProperty("lastName") private String lastName;
	@JsonProperty("loanType") private LoanType loanType;
	@JsonProperty("loanStatus") private LoanStatus loanStatus;
	@JsonProperty("requestLoanAmount") private double requestLoanAmount;
	@JsonProperty("salary") private double salary;
	@JsonProperty("creditScore") private int creditScore;
	@JsonProperty("companyName") private String companyName;
	@JsonProperty("created") private Timestamp created;
	@JsonProperty("lastUpdated") private Timestamp lastUpdated;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getLoanId() {
		return loanId;
	}

	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public LoanStatus getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(LoanStatus loanStatus) {
		this.loanStatus = loanStatus;
	}

	public double getRequestLoanAmount() {
		return requestLoanAmount;
	}

	public void setRequestLoanAmount(double requestLoanAmount) {
		this.requestLoanAmount = requestLoanAmount;
	}

	public String  getUserId() {
		return userId;
	}

	public void setUserId(String  userId) {
		this.userId = userId;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public static class LoanDataBuilder {
		private long loanId;
		private String firstName;
		private String lastName;
		private LoanType loanType;
		private LoanStatus loanStatus;
		private double requestLoanAmount;
		private String userId;
		private double salary;
		private int creditScore;
		private String companyName;
		private Timestamp created;
		private Timestamp lastUpdated;

		public LoanDataBuilder withLoanId(long loanId) {
			this.loanId = loanId;
			return this;
		}

		public LoanDataBuilder withLoanType(LoanType loanType) {
			this.loanType = loanType;
			return this;
		}

		public LoanDataBuilder withLoanStatus(LoanStatus loanStatus) {
			this.loanStatus = loanStatus;
			return this;
		}

		public LoanDataBuilder withRequestLoanAmount(double requestLoanAmount) {
			this.requestLoanAmount = requestLoanAmount;
			return this;
		}

		public LoanDataBuilder withUserId(String  userId) {
			this.userId = userId;
			return this;
		}

		public LoanDataBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public LoanDataBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public LoanDataBuilder withSalary(double salary) {
			this.salary = salary;
			return this;
		}

		public LoanDataBuilder withCreditScore(int creditScore) {
			this.creditScore = creditScore;
			return this;
		}

		public LoanDataBuilder withCompanyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public LoanDataBuilder withCreated(Timestamp created) {
			this.created = created;
			return this;
		}

		public LoanDataBuilder withLastUpdated(Timestamp lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public LoanData build() {
			LoanData loanData = new LoanData();
			loanData.setLoanId(loanId);
			loanData.setFirstName(firstName);
			loanData.setLastName(lastName);
			loanData.setLoanType(loanType);
			loanData.setLoanStatus(loanStatus);
			loanData.setRequestLoanAmount(requestLoanAmount);
			loanData.setUserId(userId);
			loanData.setSalary(salary);
			loanData.setCreditScore(creditScore);
			loanData.setCompanyName(companyName);
			loanData.setCreated(created);
			loanData.setLastUpdated(lastUpdated);
			return loanData;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoanData loanData = (LoanData) o;
		return loanId == loanData.loanId &&
				Double.compare(loanData.requestLoanAmount, requestLoanAmount) == 0 &&
				Double.compare(loanData.salary, salary) == 0 &&
				creditScore == loanData.creditScore &&
				Objects.equal(userId, loanData.userId) &&
				Objects.equal(firstName, loanData.firstName) &&
				Objects.equal(lastName, loanData.lastName) &&
				loanType == loanData.loanType &&
				loanStatus == loanData.loanStatus &&
				Objects.equal(companyName, loanData.companyName) &&
				Objects.equal(created, loanData.created) &&
				Objects.equal(lastUpdated, loanData.lastUpdated);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(loanId, userId, firstName, lastName, loanType, loanStatus, requestLoanAmount, salary, creditScore, companyName, created, lastUpdated);
	}
}
