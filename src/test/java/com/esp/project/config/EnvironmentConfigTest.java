package com.esp.project.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnvironmentConfigTest {
	private final String name = "testname";
	private EnvironmentConfig environmentConfig;

	@Before
	public void setUp() { environmentConfig = new EnvironmentConfig(name); }

	@Test
	public void testGetName() {
		assertEquals(name, environmentConfig.getName());
	}
}
