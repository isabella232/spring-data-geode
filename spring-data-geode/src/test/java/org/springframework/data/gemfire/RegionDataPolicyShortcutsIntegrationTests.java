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

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.EvictionAction;
import org.apache.geode.cache.EvictionAlgorithm;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionShortcut;

import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests testing the use of {@link RegionShortcut} in SDG XML namespace configuration metadata.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.DataPolicy
 * @see org.apache.geode.cache.Region
 * @see org.apache.geode.cache.RegionShortcut
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.data.gemfire.tests.mock.context.GemFireMockObjectsApplicationContextInitializer
 * @see org.springframework.test.context.ContextConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 1.4.0
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "region-datapolicy-shortcuts.xml",
	initializers = GemFireMockObjectsApplicationContextInitializer.class)
@SuppressWarnings("unused")
public class RegionDataPolicyShortcutsIntegrationTests extends IntegrationTestsSupport {

	@Resource(name = "LocalWithDataPolicy")
	private Region<?, ?> localWithDataPolicy;

	@Resource(name = "LocalWithShortcut")
	private Region<?, ?> localWithShortcut;

	@Resource(name = "PartitionWithDataPolicy")
	private Region<?, ?> partitionWithDataPolicy;

	@Resource(name = "PartitionWithShortcut")
	private Region<?, ?> partitionWithShortcut;

	@Resource(name = "ReplicateWithDataPolicy")
	private Region<?, ?> replicateWithDataPolicy;

	@Resource(name = "ReplicateWithShortcut")
	private Region<?, ?> replicateWithShortcut;

	@Resource(name = "ShortcutDefaults")
	private Region<?, ?> shortcutDefaults;

	@Resource(name = "ShortcutOverrides")
	private Region<?, ?> shortcutOverrides;

	@Test
	public void testLocalRegionWithDataPolicy() {

		assertThat(localWithDataPolicy)
			.describedAs("A reference to the 'LocalWithDataPolicy' Region was not property configured!")
			.isNotNull();

		assertThat(localWithDataPolicy.getName()).isEqualTo("LocalWithDataPolicy");
		assertThat(localWithDataPolicy.getFullPath()).isEqualTo("/LocalWithDataPolicy");
		assertThat(localWithDataPolicy.getAttributes()).isNotNull();
		assertThat(localWithDataPolicy.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.NORMAL);
	}

	@Test
	public void testLocalRegionWithShortcut() {

		assertThat(localWithShortcut)
			.describedAs("A reference to the 'LocalWithShortcut' Region was not property configured!")
			.isNotNull();

		assertThat(localWithShortcut.getName()).isEqualTo("LocalWithShortcut");
		assertThat(localWithShortcut.getFullPath()).isEqualTo("/LocalWithShortcut");
		assertThat(localWithShortcut.getAttributes()).isNotNull();
		assertThat(localWithShortcut.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PERSISTENT_REPLICATE);
	}

	@Test
	public void testPartitionRegionWithDataPolicy() {

		assertThat(partitionWithDataPolicy)
			.describedAs("A reference to the 'PartitionWithDataPolicy' Region was not property configured!")
			.isNotNull();

		assertThat(partitionWithDataPolicy.getName()).isEqualTo("PartitionWithDataPolicy");
		assertThat(partitionWithDataPolicy.getFullPath()).isEqualTo("/PartitionWithDataPolicy");
		assertThat(partitionWithDataPolicy.getAttributes()).isNotNull();
		assertThat(partitionWithDataPolicy.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PARTITION);
	}

	@Test
	public void testPartitionRegionWithShortcut() {

		assertThat(partitionWithShortcut)
			.describedAs("A reference to the 'PartitionWithShortcut' Region was not property configured!")
			.isNotNull();

		assertThat(partitionWithShortcut.getName()).isEqualTo("PartitionWithShortcut");
		assertThat(partitionWithShortcut.getFullPath()).isEqualTo("/PartitionWithShortcut");
		assertThat(partitionWithShortcut.getAttributes()).isNotNull();
		assertThat(partitionWithShortcut.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PERSISTENT_PARTITION);
	}

	@Test
	public void testReplicateRegionWithDataPolicy() {

		assertThat(replicateWithDataPolicy)
			.describedAs("A reference to the 'ReplicateWithDataPolicy' Region was not property configured!")
			.isNotNull();

		assertThat(replicateWithDataPolicy.getName()).isEqualTo("ReplicateWithDataPolicy");
		assertThat(replicateWithDataPolicy.getFullPath()).isEqualTo("/ReplicateWithDataPolicy");
		assertThat(replicateWithDataPolicy.getAttributes()).isNotNull();
		assertThat(replicateWithDataPolicy.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.REPLICATE);
	}

	@Test
	public void testReplicateRegionWithShortcut() {

		assertThat(replicateWithShortcut)
			.describedAs("A reference to the 'ReplicateWithShortcut' Region was not property configured!")
			.isNotNull();

		assertThat(replicateWithShortcut.getName()).isEqualTo("ReplicateWithShortcut");
		assertThat(replicateWithShortcut.getFullPath()).isEqualTo("/ReplicateWithShortcut");
		assertThat(replicateWithShortcut.getAttributes()).isNotNull();
		assertThat(replicateWithShortcut.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PERSISTENT_REPLICATE);
	}

	@Test
	public void testShortcutDefaultsRegion() {

		assertThat(shortcutDefaults)
			.describedAs("A reference to the 'ShortcutDefaults' Region was not properly configured!")
			.isNotNull();

		assertThat(shortcutDefaults.getName()).isEqualTo("ShortcutDefaults");
		assertThat(shortcutDefaults.getFullPath()).isEqualTo("/ShortcutDefaults");
		assertThat(shortcutDefaults.getAttributes()).isNotNull();
		assertThat(shortcutDefaults.getAttributes().getCloningEnabled()).isFalse();
		assertThat(shortcutDefaults.getAttributes().getConcurrencyChecksEnabled()).isTrue();
		assertThat(shortcutDefaults.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PERSISTENT_PARTITION);
		assertThat(shortcutDefaults.getAttributes().isDiskSynchronous()).isFalse();
		assertThat(shortcutDefaults.getAttributes().getIgnoreJTA()).isTrue();
		assertThat(shortcutDefaults.getAttributes().getInitialCapacity()).isEqualTo(101);
		assertThat(new Float(shortcutDefaults.getAttributes().getLoadFactor())).isEqualTo(new Float(0.85f));
		assertThat(shortcutDefaults.getAttributes().getKeyConstraint()).isEqualTo(Long.class);
		assertThat(shortcutDefaults.getAttributes().getValueConstraint()).isEqualTo(String.class);
		assertThat(shortcutDefaults.getAttributes().getEvictionAttributes()).isNotNull();
		assertThat(shortcutDefaults.getAttributes().getEvictionAttributes().getAction()).isEqualTo(EvictionAction.OVERFLOW_TO_DISK);
		assertThat(shortcutDefaults.getAttributes().getEvictionAttributes().getAlgorithm()).isEqualTo(EvictionAlgorithm.LRU_HEAP);
		assertThat(shortcutDefaults.getAttributes().getPartitionAttributes()).isNotNull();
		assertThat(shortcutDefaults.getAttributes().getPartitionAttributes().getRedundantCopies()).isEqualTo(1);
		assertThat(shortcutDefaults.getAttributes().getPartitionAttributes().getTotalNumBuckets()).isEqualTo(177);
	}

	@Test
	public void testShortcutOverridesRegion() {

		assertThat(shortcutOverrides)
			.describedAs("A reference to the 'ShortcutOverrides' Region was not properly configured!")
			.isNotNull();

		assertThat(shortcutOverrides.getName()).isEqualTo("ShortcutOverrides");
		assertThat(shortcutOverrides.getFullPath()).isEqualTo("/ShortcutOverrides");
		assertThat(shortcutOverrides.getAttributes()).isNotNull();
		assertThat(shortcutOverrides.getAttributes().getCloningEnabled()).isTrue();
		assertThat(shortcutOverrides.getAttributes().getConcurrencyChecksEnabled()).isFalse();
		assertThat(shortcutOverrides.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.PARTITION);
		assertThat(shortcutOverrides.getAttributes().isDiskSynchronous()).isTrue();
		assertThat(shortcutOverrides.getAttributes().getIgnoreJTA()).isFalse();
		assertThat(shortcutOverrides.getAttributes().getInitialCapacity()).isEqualTo(51);
		assertThat(new Float(shortcutOverrides.getAttributes().getLoadFactor())).isEqualTo(new Float(0.72f));
		assertThat(shortcutOverrides.getAttributes().getKeyConstraint()).isEqualTo(String.class);
		assertThat(shortcutOverrides.getAttributes().getValueConstraint()).isEqualTo(Object.class);
		assertThat(shortcutOverrides.getAttributes().getEvictionAttributes()).isNotNull();
		assertThat(shortcutOverrides.getAttributes().getEvictionAttributes().getAction()).isEqualTo(EvictionAction.LOCAL_DESTROY);
		assertThat(shortcutOverrides.getAttributes().getEvictionAttributes().getMaximum()).isEqualTo(8192);
		assertThat(shortcutOverrides.getAttributes().getEvictionAttributes().getAlgorithm()).isEqualTo(EvictionAlgorithm.LRU_ENTRY);
		assertThat(shortcutOverrides.getAttributes().getPartitionAttributes()).isNotNull();
		assertThat(shortcutOverrides.getAttributes().getPartitionAttributes().getRedundantCopies()).isEqualTo(3);
		assertThat(shortcutOverrides.getAttributes().getPartitionAttributes().getTotalNumBuckets()).isEqualTo(111);
	}
}
