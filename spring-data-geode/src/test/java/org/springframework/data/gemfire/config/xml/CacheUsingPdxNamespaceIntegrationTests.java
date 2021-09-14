/*
 * Copyright 2010-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.gemfire.config.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.pdx.PdxSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.config.support.PdxDiskStoreAwareBeanFactoryPostProcessor;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests with test case testing the SDG XML namespace configuration metadata when PDX is configured
 * in Apache Geode.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.pdx.PdxSerializer
 * @see org.springframework.data.gemfire.config.support.PdxDiskStoreAwareBeanFactoryPostProcessor
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 1.3.3
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "cache-using-pdx-ns.xml",
	initializers = GemFireMockObjectsApplicationContextInitializer.class)
@SuppressWarnings("unused")
public class CacheUsingPdxNamespaceIntegrationTests extends IntegrationTestsSupport {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testApplicationContextHasPdxDiskStoreAwareBeanFactoryPostProcessor() {

		PdxDiskStoreAwareBeanFactoryPostProcessor postProcessor =
			applicationContext.getBean(PdxDiskStoreAwareBeanFactoryPostProcessor.class);

		// NOTE the postProcessor reference will not be null as the ApplicationContext.getBean(:Class) method (getting
		// a bean by Class type) will throw a NoSuchBeanDefinitionException if no bean of type
		// PdxDiskStoreAwareBeanFactoryPostProcessor could be found, or throw a NoUniqueBeanDefinitionException if
		// our PdxDiskStoreAwareBeanFactoryPostProcessor bean is not unique!
		assertThat(postProcessor).isNotNull();
		assertThat(postProcessor.getPdxDiskStoreName()).isEqualTo("pdxStore");
	}

	@Test
	public void testCachePdxConfiguration() {

		CacheFactoryBean cacheFactoryBean = applicationContext.getBean("&gemfireCache", CacheFactoryBean.class);

		assertThat(cacheFactoryBean).isNotNull();
		assertThat(cacheFactoryBean.getPdxDiskStoreName()).isEqualTo("pdxStore");
		assertThat(Boolean.TRUE.equals(cacheFactoryBean.getPdxPersistent())).isTrue();
		assertThat(Boolean.TRUE.equals(cacheFactoryBean.getPdxReadSerialized())).isTrue();

		PdxSerializer autoSerializer = applicationContext.getBean("autoSerializer", PdxSerializer.class);

		assertThat(autoSerializer).isNotNull();
		assertThat(cacheFactoryBean.getPdxSerializer()).isSameAs(autoSerializer);
	}

}