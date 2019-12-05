package com.esp.project.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AwsConfigTest {

	private final String accessKey = "testAccessKey";
	private final String secretKey = "testSecretKey";

	private AwsConfig awsConfig;

	@Before
	public void setUp() {
		awsConfig = new AwsConfig(accessKey, secretKey);
	}

	@Test
	public void testGetAccessKet() {
		assertEquals(accessKey, awsConfig.getAccessKey());
	}

	@Test
	public void testGetSecretKet() {
		assertEquals(secretKey, awsConfig.getSecretKey());
	}
}
