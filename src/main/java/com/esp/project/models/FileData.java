package com.esp.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileData {
	@JsonProperty("fileName") private String fileName;
	@JsonProperty("fileTempUrl") private String fileTempUrl;

	public FileData(final String fileName, final String fileTempUrl) {
		this.fileName = fileName;
		this.fileTempUrl = fileTempUrl;
	}

	@Override
	public String toString() {
		return "{" +
				"fileName='" + fileName + '\'' +
				", fileTempUrl='" + fileTempUrl + '\'' +
				'}';
	}
}

