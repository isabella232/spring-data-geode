/*
 * Copyright 2012-2021 the original author or authors.
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
package org.springframework.data.gemfire.config.support;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.Pool;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.data.gemfire.util.SpringUtils;

/**
 * {@link ClientRegionPoolBeanFactoryPostProcessor} is a Spring {@link BeanFactoryPostProcessor} implementation
 * ensuring a proper dependency is declared between a client {@link Region} and a client {@link Pool} it references
 * and uses, providing the client {@link Pool} has been defined and configured with Spring Data for Apache Geode
 * configuration metadata (e.g. XML).
 *
 * @author John Blum
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.client.Pool
 * @see org.springframework.beans.factory.config.BeanDefinition
 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor
 * @see org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * @since 1.8.2
 */
public class ClientRegionPoolBeanFactoryPostProcessor extends AbstractDependencyStructuringBeanFactoryPostProcessor {

	protected static final String POOL_NAME_PROPERTY = "poolName";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Set<String> clientRegionBeanNames = new HashSet<>();
		Set<String> poolBeanNames = new HashSet<>();

		Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(beanName -> {

			BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

			if (isClientRegionBean(beanDefinition)) {
				clientRegionBeanNames.add(beanName);
			}
			else if (isPoolBean(beanDefinition)) {
				poolBeanNames.add(beanName);
			}
		});

		clientRegionBeanNames.forEach(clientRegionBeanName -> {

			BeanDefinition clientRegionBean = beanFactory.getBeanDefinition(clientRegionBeanName);

			String poolName = getPoolName(clientRegionBean);

			if (poolBeanNames.contains(poolName)) {
				SpringUtils.addDependsOn(clientRegionBean, poolName);
			}
		});
	}

	String getPoolName(BeanDefinition clientRegionBean) {

		return Optional.ofNullable(clientRegionBean.getPropertyValues().getPropertyValue(POOL_NAME_PROPERTY))
			.map(PropertyValue::getValue)
			.map(String::valueOf)
			.orElse(null);
	}
}
