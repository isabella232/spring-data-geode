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
package org.springframework.data.gemfire.repository.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.GemfireMappingContext;
import org.springframework.data.gemfire.mapping.GemfirePersistentEntity;
import org.springframework.data.gemfire.repository.sample.Algorithm;
import org.springframework.data.gemfire.repository.sample.Animal;

/**
 * Unit Tests for {@link DefaultGemfireEntityInformation}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.springframework.data.gemfire.repository.query.DefaultGemfireEntityInformation
 * @since 1.4.0
 */
public class DefaultGemfireEntityInformationUnitTests {

	private GemfireMappingContext mappingContext;

	@Before
	public void setup() {
		mappingContext = new GemfireMappingContext();
	}

	@SuppressWarnings("unchecked")
	protected <T extends Algorithm> T newAlgorithm(String name) {
		return (T) (Algorithm) () -> name;
	}

	protected Animal newAnimal(Long id, String name) {
		Animal animal = new Animal();
		animal.setId(id);
		animal.setName(name);
		return animal;
	}

	protected <T, ID extends Serializable> GemfireEntityInformation<T, ID> newEntityInformation(
			GemfirePersistentEntity<T> persistentEntity) {

		return new DefaultGemfireEntityInformation<>(persistentEntity);
	}

	@SuppressWarnings("unchecked")
	protected <T> GemfirePersistentEntity<T> newPersistentEntity(Class<T> entityType) {
		return (GemfirePersistentEntity<T>) mappingContext.getPersistentEntity(entityType);
	}

	@Test
	public void interfaceBasedEntity() {

		GemfireEntityInformation<Algorithm, String> entityInfo =
			newEntityInformation(newPersistentEntity(Algorithm.class));

		assertThat(entityInfo).isNotNull();
		assertThat(entityInfo.getRegionName()).isEqualTo("Algorithms");
		assertThat(Algorithm.class.isAssignableFrom(entityInfo.getJavaType())).isTrue();
		assertThat(entityInfo.getIdType()).isEqualTo(String.class);
		assertThat(entityInfo.getId(new QuickSort())).isEqualTo("QuickSort");
		assertThat(entityInfo.getId(newAlgorithm("Quick Sort"))).isEqualTo("Quick Sort");
	}

	@Test
	public void classBasedEntity() {

		GemfireEntityInformation<Animal, Long> entityInfo =
			newEntityInformation(newPersistentEntity(Animal.class));

		assertThat(entityInfo).isNotNull();
		assertThat(entityInfo.getRegionName()).isEqualTo("Animal");
		assertThat(entityInfo.getJavaType()).isEqualTo(Animal.class);
		assertThat(entityInfo.getIdType()).isEqualTo(Long.class);
		assertThat(entityInfo.getId(newAnimal(1L, "Tyger"))).isEqualTo(1L);
	}

	@Test
	public void confusedDomainEntityTypedWithLongId() {

		GemfireEntityInformation<ConfusedDomainEntity, Long> entityInfo =
			newEntityInformation(newPersistentEntity(ConfusedDomainEntity.class));

		assertThat(entityInfo).isNotNull();
		assertThat(entityInfo.getRegionName()).isEqualTo("ConfusedDomainEntity");
		assertThat(entityInfo.getJavaType()).isEqualTo(ConfusedDomainEntity.class);
		assertThat(entityInfo.getIdType()).isEqualTo(Long.class);
		assertThat(entityInfo.getId(new ConfusedDomainEntity(123L))).isEqualTo(123L);
	}

	@Test
	public void confusedDomainEntityTypedStringId() {

		GemfireEntityInformation<ConfusedDomainEntity, ?> entityInfo =
			newEntityInformation(newPersistentEntity(ConfusedDomainEntity.class));

		assertThat(entityInfo).isNotNull();
		assertThat(entityInfo.getRegionName()).isEqualTo("ConfusedDomainEntity");
		assertThat(entityInfo.getJavaType()).isEqualTo(ConfusedDomainEntity.class);
		//assertEquals(String.class, entityInfo.getIdType());
		assertThat(Long.class.equals(entityInfo.getIdType())).isTrue();
		assertThat(entityInfo.getId(new ConfusedDomainEntity(123L))).isEqualTo(123L);
		assertThat(entityInfo.getId(new ConfusedDomainEntity("248"))).isEqualTo(248L);
	}

	@SuppressWarnings("unused")
	protected static class ConfusedDomainEntity {

		@Id
		private Long id;

		protected ConfusedDomainEntity(Long id) {
			this.id = id;
		}

		protected ConfusedDomainEntity(String id) {
			setId(id);
		}

		@Id
		protected String getId() {
			return String.valueOf(id);
		}

		protected final void setId(String id) {
			try {
				this.id = Long.valueOf(id);
			}
			catch (NumberFormatException e) {
				this.id = null;
			}
		}
	}

	protected static class QuickSort implements Algorithm {

		@Override
		public String getName() {
			return getClass().getSimpleName();
		}
	}
}
