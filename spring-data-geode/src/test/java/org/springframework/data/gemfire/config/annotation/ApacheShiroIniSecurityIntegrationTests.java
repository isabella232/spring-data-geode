/*
 * Copyright 2016-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.springframework.data.gemfire.config.annotation;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration tests for Apache Geode Integrated Security using an Apache Shiro INI security configuration resource.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.springframework.data.gemfire.config.annotation.AbstractGeodeSecurityIntegrationTests
 * @see org.springframework.test.context.ActiveProfiles
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 2.0.0
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("apache-geode-client")
@ContextConfiguration(classes = AbstractGeodeSecurityIntegrationTests.GeodeClientConfiguration.class)
@DirtiesContext
public class ApacheShiroIniSecurityIntegrationTests extends AbstractGeodeSecurityIntegrationTests {

	protected static final String SHIRO_INI_CONFIGURATION_PROFILE = "shiro-ini-configuration";

	@BeforeClass
	public static void startGeodeServer() throws IOException {
		runGeodeServer(SHIRO_INI_CONFIGURATION_PROFILE);
	}

	@Configuration
	@EnableSecurity(shiroIniResourcePath = "shiro.ini")
	@Profile(SHIRO_INI_CONFIGURATION_PROFILE)
	public static class ApacheShiroIniConfiguration { }

}
