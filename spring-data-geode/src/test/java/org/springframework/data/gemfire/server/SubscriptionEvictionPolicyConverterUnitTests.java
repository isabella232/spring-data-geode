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
package org.springframework.data.gemfire.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for {@link SubscriptionEvictionPolicyConverter}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.springframework.data.gemfire.server.SubscriptionEvictionPolicy
 * @see org.springframework.data.gemfire.server.SubscriptionEvictionPolicyConverter
 * @since 1.6.0
 */
public class SubscriptionEvictionPolicyConverterUnitTests {

	private final SubscriptionEvictionPolicyConverter converter = new SubscriptionEvictionPolicyConverter();

	@After
	public void tearDown() {
		converter.setValue(null);
	}

	@Test
	public void convert() {

		assertThat(converter.convert("EnTry")).isEqualTo(SubscriptionEvictionPolicy.ENTRY);
		assertThat(converter.convert("MEM")).isEqualTo(SubscriptionEvictionPolicy.MEM);
		assertThat(converter.convert("nONE")).isEqualTo(SubscriptionEvictionPolicy.NONE);
		assertThat(converter.convert("NOne")).isEqualTo(SubscriptionEvictionPolicy.NONE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void convertIllegalValue() {

		try {
			converter.setAsText("memory");
		}
		catch (IllegalArgumentException expected) {

			assertThat(expected).hasMessage("[memory] is not a valid SubscriptionEvictionPolicy");
			assertThat(expected).hasNoCause();

			throw expected;
		}
	}

	@Test
	public void setAsText() {

		assertThat(converter.getValue()).isNull();

		converter.setAsText("enTRY");

		assertThat(converter.getValue()).isEqualTo(SubscriptionEvictionPolicy.ENTRY);

		converter.setAsText("MEm");

		assertThat(converter.getValue()).isEqualTo(SubscriptionEvictionPolicy.MEM);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setAsTextWithIllegalValue() {

		try {
			converter.setAsText("KEYS");
		}
		catch (IllegalArgumentException expected) {

			assertThat(expected).hasMessage("[KEYS] is not a valid SubscriptionEvictionPolicy");
			assertThat(expected).hasNoCause();

			throw expected;
		}
	}
}
