/*
 * Copyright 2010-2021 the original author or authors.
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
 */
package org.springframework.data.gemfire.config.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.InterestPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionAttributes;
import org.apache.geode.cache.SubscriptionAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.gemfire.PeerRegionFactoryBean;
import org.springframework.data.gemfire.TestUtils;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests ensuring subscription policy can be applied to server {@link Region Regions}.
 *
 * @author Lyndon Adams
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.SubscriptionAttributes
 * @see org.springframework.data.gemfire.SubscriptionAttributesFactoryBean
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 1.3.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "subscription-ns.xml",
	initializers = GemFireMockObjectsApplicationContextInitializer.class)
@SuppressWarnings({ "rawtypes", "unused" })
public class CacheSubscriptionTest extends IntegrationTestsSupport {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testReplicatedRegionSubscriptionAllPolicy() throws Exception {

		assertThat(applicationContext.containsBean("replicALL")).isTrue();

		PeerRegionFactoryBean regionFactoryBean = applicationContext.getBean("&replicALL", PeerRegionFactoryBean.class);
		RegionAttributes regionAttributes = TestUtils.readField("attributes", regionFactoryBean);

		assertThat(regionAttributes).isNotNull();

		SubscriptionAttributes subscriptionAttributes = regionAttributes.getSubscriptionAttributes();

		assertThat(subscriptionAttributes).isNotNull();
		assertThat(subscriptionAttributes.getInterestPolicy()).isEqualTo(InterestPolicy.ALL);
	}

	@Test
	public void testPartitionRegionSubscriptionCacheContentPolicy() throws Exception {

		assertThat(applicationContext.containsBean("partCACHE_CONTENT")).isTrue();

		PeerRegionFactoryBean regionFactoryBean = applicationContext.getBean("&partCACHE_CONTENT", PeerRegionFactoryBean.class);
		RegionAttributes regionAttributes = TestUtils.readField("attributes", regionFactoryBean);

		assertThat(regionAttributes).isNotNull();

		SubscriptionAttributes subscriptionAttributes = regionAttributes.getSubscriptionAttributes();

		assertThat(subscriptionAttributes).isNotNull();
		assertThat(subscriptionAttributes.getInterestPolicy()).isEqualTo(InterestPolicy.CACHE_CONTENT);
	}

	@Test
	public void testPartitionRegionSubscriptionDefaultPolicy() throws Exception {

		assertThat(applicationContext.containsBean("partDEFAULT")).isTrue();

		PeerRegionFactoryBean regionFactoryBean = applicationContext.getBean("&partDEFAULT", PeerRegionFactoryBean.class);
		RegionAttributes regionAttributes = TestUtils.readField("attributes", regionFactoryBean);

		assertThat(regionAttributes).isNotNull();

		SubscriptionAttributes subscriptionAttributes = regionAttributes.getSubscriptionAttributes();

		assertThat(subscriptionAttributes).isNotNull();
		assertThat(subscriptionAttributes.getInterestPolicy()).isEqualTo(InterestPolicy.DEFAULT);
	}
}
