package com.esp.project.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvironmentConfig {
	private final String name;

	@JsonCreator
	public EnvironmentConfig(@JsonProperty("name") final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
