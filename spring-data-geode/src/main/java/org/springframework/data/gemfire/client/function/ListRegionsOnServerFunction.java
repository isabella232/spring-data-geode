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
package org.springframework.data.gemfire.client.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

/**
 * ListRegionsOnServerFunction is a GemFire Function class that returns a List of names for all Regions
 * defined in the GemFire cluster.
 *
 * @author David Turanski
 * @author John Blum
 * @see org.apache.geode.cache.execute.Function
 */
@SuppressWarnings("serial")
public class ListRegionsOnServerFunction implements Function {

	private static final long serialVersionUID = 867530169L;

	public static final String ID = ListRegionsOnServerFunction.class.getName();

	@Override
	@SuppressWarnings("unchecked")
	public void execute(FunctionContext functionContext) {

		List<String> regionNames = new ArrayList<>();

		for (Region<?, ?> region : getCache().rootRegions()) {
			regionNames.add(region.getName());
		}

		functionContext.getResultSender().lastResult(regionNames);
	}

	Cache getCache() {
		return CacheFactory.getAnyInstance();
	}

	@Override
	public String getId() {
		return this.getClass().getName();
	}

	@Override
	public boolean hasResult() {
		return true;
	}

	@Override
	public boolean isHA() {
		return false;
	}

	@Override
	public boolean optimizeForWrite() {
		return false;
	}
}
