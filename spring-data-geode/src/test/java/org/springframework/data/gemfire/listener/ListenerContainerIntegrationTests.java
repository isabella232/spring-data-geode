/*
 * Copyright 2011-2021 the original author or authors.
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
package org.springframework.data.gemfire.listener;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EventListener;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.query.CqEvent;

import org.springframework.data.gemfire.fork.CqCacheServerProcess;
import org.springframework.data.gemfire.listener.adapter.ContinuousQueryListenerAdapter;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.tests.process.ProcessWrapper;
import org.springframework.data.gemfire.util.SpringUtils;

/**
 * @author Costin Leau
 * @author John Blum
 */
public class ListenerContainerIntegrationTests extends ForkingClientServerIntegrationTestsSupport {

	private ClientCache gemfireCache = null;

	private final ContinuousQueryListenerAdapter adapter = new ContinuousQueryListenerAdapter(new EventListener() {

		@SuppressWarnings("unused")
		public void handleEvent(CqEvent event) {
			cqEvents.add(event);
		}
	});

	private ContinuousQueryListenerContainer container;

	private final List<CqEvent> cqEvents = new CopyOnWriteArrayList<>();

	@BeforeClass
	public static void startGemFireServer() throws Exception {
		startGemFireServer(CqCacheServerProcess.class);
	}

	@Before
	public void setupGemFireClient() {

		gemfireCache = new ClientCacheFactory()
			.set("name", "ListenerContainerIntegrationTests")
			.set("log-level", "error")
			.setPoolSubscriptionEnabled(true)
			.addPoolServer(DEFAULT_HOSTNAME, Integer.getInteger(GEMFIRE_CACHE_SERVER_PORT_PROPERTY))
			.create();

		String query = "SELECT * from /test-cq";

		container = new ContinuousQueryListenerContainer();
		container.setBeanName("cqListenerContainer");
		container.setCache(gemfireCache);
		container.afterPropertiesSet();
		container.addListener(new ContinuousQueryDefinition("test", query, adapter));
		container.start();
	}

	@After
	public void closeGemFireClient() {

		Optional.ofNullable(this.gemfireCache)
			.ifPresent(cache -> SpringUtils.safeDoOperation(() -> cache.close()));
	}

	@Test
	public void testContainer() {

		getGemFireServerProcess().ifPresent(ProcessWrapper::signal);

		waitOn(() -> this.cqEvents.size() == 3, TimeUnit.SECONDS.toMillis(5));

		assertThat(this.cqEvents.size()).isEqualTo(3);
	}
}
