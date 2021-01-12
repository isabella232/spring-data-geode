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

package org.springframework.data.gemfire.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.gemfire.util.CollectionUtils.asSet;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration tests using XML for {@link GemfireBeanFactoryLocator}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.data.gemfire.support.GemfireBeanFactoryLocator
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 2.0.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class GemfireBeanFactoryLocatorIntegrationTests {

	@Autowired
	@SuppressWarnings("unused")
	private BeanFactory beanFactory;

	@Test
	public void beanFactoryContainsGemfireBeanFactoryLocatorBean() {
		assertThat(beanFactory.containsBean(GemfireBeanFactoryLocator.class.getName())).isTrue();

		GemfireBeanFactoryLocator gemfireBeanFactoryLocator =
			beanFactory.getBean(GemfireBeanFactoryLocator.class.getName(), GemfireBeanFactoryLocator.class);

		assertThat(gemfireBeanFactoryLocator).isNotNull();
		assertThat(gemfireBeanFactoryLocator.getBeanFactory()).isSameAs(beanFactory);
		assertThat(gemfireBeanFactoryLocator.getAssociatedBeanName())
			.startsWith(GemfireBeanFactoryLocator.class.getName());
		assertThat(gemfireBeanFactoryLocator.getAssociatedBeanNameWithAliases()).isNotNull();
		assertThat(gemfireBeanFactoryLocator.getAssociatedBeanNameWithAliases())
			.containsAll(asSet(GemfireBeanFactoryLocator.class.getName()));
		assertThat(beanFactory.getAliases(GemfireBeanFactoryLocator.class.getName()))
			.containsAll(asSet(GemfireBeanFactoryLocator.class.getName().concat("#0")));
	}

	@Test
	public void beanFactoryContainsTestBeanFactoryLocatorBean() {
		assertThat(beanFactory.containsBean("testBeanFactoryLocator")).isTrue();

		GemfireBeanFactoryLocator testBeanFactoryLocator = beanFactory.getBean("testBeanFactoryLocator",
			GemfireBeanFactoryLocator.class);

		assertThat(testBeanFactoryLocator).isNotNull();
		assertThat(testBeanFactoryLocator.getBeanFactory()).isSameAs(beanFactory);
		assertThat(testBeanFactoryLocator.getAssociatedBeanName()).isEqualTo("testBeanFactoryLocator");
		assertThat(testBeanFactoryLocator.getAssociatedBeanNameWithAliases()).isNotNull();
		assertThat(testBeanFactoryLocator.getAssociatedBeanNameWithAliases())
			.containsAll(asSet("testBeanFactoryLocator", "aliasOne", "aliasTwo"));
		assertThat(beanFactory.getAliases("testBeanFactoryLocator")).containsAll(asSet("aliasOne", "aliasTwo"));
	}

	@Test
	public void registeredBeanFactoriesIsCorrect() {
		Set<String> beanNames = asSet("gemfire-cache", "gemfireCache", "testBeanFactoryLocator", "aliasOne", "aliasTwo",
			GemfireBeanFactoryLocator.class.getName());

		assertThat(GemfireBeanFactoryLocator.BEAN_FACTORIES.keySet()).containsAll(beanNames);

		for (String beanName : beanNames) {
			assertThat(GemfireBeanFactoryLocator.BEAN_FACTORIES.get(beanName)).isSameAs(beanFactory);
		}
	}
}
