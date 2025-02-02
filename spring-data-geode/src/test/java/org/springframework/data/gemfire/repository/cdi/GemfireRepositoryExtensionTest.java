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
package org.springframework.data.gemfire.repository.cdi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Qualifier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import org.apache.geode.cache.Region;

import org.springframework.data.gemfire.mapping.GemfireMappingContext;
import org.springframework.data.gemfire.repository.GemfireRepository;

/**
 * The GemfireRepositoryExtensionTest class is a test suite of unit tests testing the contract and proper functionality
 * of the {@link GemfireRepositoryExtension} class in a Java EE CDI context.
 *
 * @author John Blum
 * @see javax.enterprise.inject.spi.Bean
 * @see org.junit.Test
 * @see org.mockito.Mockito
 * @see org.springframework.data.gemfire.mapping.GemfireMappingContext
 * @see org.springframework.data.gemfire.repository.GemfireRepository
 * @see org.springframework.data.gemfire.repository.cdi.GemfireRepositoryExtension
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class GemfireRepositoryExtensionTest {

	private GemfireRepositoryExtension repositoryExtension;

	@Before
	public void setup() {
		repositoryExtension = new GemfireRepositoryExtension();
	}

	protected <T> Set<T> asSet(T... array) {
		return new HashSet<>(Arrays.asList(array));
	}

	@SuppressWarnings("rawtypes")
	private Annotation mockAnnotation(Class annotationType) {
		Annotation mockAnnotation = mock(Annotation.class);
		when(mockAnnotation.annotationType()).thenReturn(annotationType);
		return mockAnnotation;
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void processBeanIdentifiesAndProcessesRegionBeanCorrectly() {

		ProcessBean<Region> mockProcessBean = mock(ProcessBean.class);

		Bean<Region> mockBean = mock(Bean.class);

		when(mockProcessBean.getBean()).thenReturn(mockBean);
		when(mockBean.getTypes()).thenReturn(Collections.singleton(Region.class));

		assertThat(repositoryExtension.regionBeans.isEmpty()).isTrue();

		repositoryExtension.processBean(mockProcessBean);

		assertThat(repositoryExtension.regionBeans.contains(mockBean)).isTrue();

		verify(mockProcessBean, times(1)).getBean();
		verify(mockBean, times(1)).getTypes();
	}

	@Test
	public void processBeanIdentifiesAndProcessesGemfireMappingContextBeanCorrectly() {
		ProcessBean<GemfireMappingContext> mockProcessBean = mock(ProcessBean.class);
		Bean<GemfireMappingContext> mockBean = mock(Bean.class);

		Set<Annotation> expectedQualifiers = asSet(mockAnnotation(SpringDataRepo.class),
			mockAnnotation(GemfireRepo.class));

		when(mockProcessBean.getBean()).thenReturn(mockBean);
		when(mockBean.getTypes()).thenReturn(Collections.singleton(GemfireMappingContext.class));
		when(mockBean.getQualifiers()).thenReturn(expectedQualifiers);

		assertThat(repositoryExtension.mappingContexts.isEmpty()).isTrue();

		repositoryExtension.processBean(mockProcessBean);

		assertThat(repositoryExtension.mappingContexts.containsKey(expectedQualifiers)).isTrue();
		assertThat(repositoryExtension.mappingContexts.get(expectedQualifiers)).isEqualTo(mockBean);

		verify(mockProcessBean, times(1)).getBean();
		verify(mockBean, times(2)).getTypes();
		verify(mockBean, times(2)).getQualifiers();
	}

	@Test
	public void processBeanIgnoresNonRegionNonGemfireMappingContextBeansProperly() {
		ProcessBean<Object> mockProcessBean = mock(ProcessBean.class);
		Bean<Object> mockBean = mock(Bean.class);

		when(mockProcessBean.getBean()).thenReturn(mockBean);
		when(mockBean.getTypes()).thenReturn(Collections.singleton(Object.class));

		assertThat(repositoryExtension.mappingContexts.isEmpty()).isTrue();
		assertThat(repositoryExtension.regionBeans.isEmpty()).isTrue();

		repositoryExtension.processBean(mockProcessBean);

		assertThat(repositoryExtension.mappingContexts.isEmpty()).isTrue();
		assertThat(repositoryExtension.regionBeans.isEmpty()).isTrue();

		verify(mockProcessBean, times(1)).getBean();
		verify(mockBean, times(1)).getTypes();
	}

	@Test
	public void afterBeanDiscoveryRegistersRepositoryBean() {
		AfterBeanDiscovery mockAfterBeanDiscovery = mock(AfterBeanDiscovery.class);

		final Set<Annotation> expectedQualifiers = asSet(mockAnnotation(SpringDataRepo.class),
			mockAnnotation(GemfireRepo.class));

		doAnswer((Answer<Void>) invocation -> {

			GemfireRepositoryBean<?> repositoryBean = invocation.getArgument(0);

			assertThat(repositoryBean).isNotNull();
			assertThat(repositoryBean.getBeanClass()).isEqualTo(TestRepository.class);
			assertThat(repositoryBean.getQualifiers()).isEqualTo(expectedQualifiers);

			return null;
		}).when(mockAfterBeanDiscovery).addBean(isA(GemfireRepositoryBean.class));

		GemfireRepositoryExtension repositoryExtension = new GemfireRepositoryExtension() {

			@Override
			protected Iterable<Map.Entry<Class<?>, Set<Annotation>>> getRepositoryTypes() {
				return Collections.<Class<?>, Set<Annotation>>singletonMap(TestRepository.class, expectedQualifiers).entrySet();
			}
		};

		repositoryExtension.afterBeanDiscovery(mockAfterBeanDiscovery, mock(BeanManager.class));

		verify(mockAfterBeanDiscovery, times(1)).addBean(isA(GemfireRepositoryBean.class));
	}

	@Qualifier
	@interface GemfireRepo { }

	@Qualifier
	@interface SpringDataRepo { }

	@GemfireRepo
	@SpringDataRepo
	interface TestRepository extends GemfireRepository<Object, Long> { }

}
