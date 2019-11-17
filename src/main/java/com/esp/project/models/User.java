package com.esp.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonProperty("userId") private String userId;
	@JsonProperty("userRole") @Nonnull private UserRole userRole;
	@JsonProperty("firstName") @Nonnull private String firstName;
	@JsonProperty("lastName") private String lastName;
	@JsonProperty("email") @Nonnull private String email;
	@JsonProperty("phone")private String phone;
	@JsonProperty("address") private String address;
	@JsonProperty("created")private Timestamp created;
	@JsonProperty("lastUpdated") private Timestamp lastUpdated;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public static final class Builder {
		private String userId;
		private UserRole userRole;
		private String firstName;
		private String lastName;
		private String email;
		private String phone;
		private String address;
		private Timestamp created;
		private Timestamp lastUpdated;


		public static Builder anUser() {
			return new Builder();
		}

		public Builder withUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder withUserRole(UserRole userRole) {
			this.userRole = userRole;
			return this;
		}

		public Builder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public Builder withAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder withCreated(Timestamp created) {
			this.created = created;
			return this;
		}

		public Builder withLastUpdated(Timestamp lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public User build() {
			User user = new User();
			user.lastName = this.lastName;
			user.userRole = this.userRole;
			user.email = this.email;
			user.created = this.created;
			user.address = this.address;
			user.firstName = this.firstName;
			user.phone = this.phone;
			user.lastUpdated = this.lastUpdated;
			user.userId = this.userId;
			return user;
		}
	}
}
