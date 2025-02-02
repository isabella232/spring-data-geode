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
package org.springframework.data.gemfire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;

/**
 * Unit Tests for {@link IndexMaintenancePolicyConverter}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.springframework.data.gemfire.IndexMaintenancePolicyConverter
 * @see org.springframework.data.gemfire.IndexMaintenancePolicyType
 * @since 1.6.0
 */
public class IndexMaintenancePolicyConverterUnitTests {

	private final IndexMaintenancePolicyConverter converter = new IndexMaintenancePolicyConverter();

	@After
	public void tearDown() {
		converter.setValue(null);
	}

	@Test
	public void convert() {

		assertThat(converter.convert("asynchronous")).isEqualTo(IndexMaintenancePolicyType.ASYNCHRONOUS);
		assertThat(converter.convert("Synchronous")).isEqualTo(IndexMaintenancePolicyType.SYNCHRONOUS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void convertIllegalValue() {

		try {
			converter.convert("sync");
		}
		catch (IllegalArgumentException expected) {

			assertThat(expected).hasMessage("[sync] is not a valid IndexMaintenancePolicyType");
			assertThat(expected).hasNoCause();

			throw expected;
		}
	}

	@Test
	public void setAsText() {

		assertThat(converter.getValue()).isNull();

		converter.setAsText("aSynchronous");

		assertThat(converter.getValue()).isEqualTo(IndexMaintenancePolicyType.ASYNCHRONOUS);

		converter.setAsText("synchrONoUS");

		assertThat(converter.getValue()).isEqualTo(IndexMaintenancePolicyType.SYNCHRONOUS);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setAsTextWithIllegalValue() {

		try {
			converter.setAsText("async");
		}
		catch (IllegalArgumentException expected) {

			assertThat(expected).hasMessage("[async] is not a valid IndexMaintenancePolicyType");
			assertThat(expected).hasNoCause();

			throw expected;
		}
		finally {
			assertThat(converter.getValue()).isNull();
		}
	}
}
