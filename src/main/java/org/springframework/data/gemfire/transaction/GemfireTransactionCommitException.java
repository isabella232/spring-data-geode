/*
 * Copyright 2020-2021 the original author or authors.
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

package org.springframework.data.gemfire.transaction;

import org.springframework.transaction.TransactionException;

/**
 * Gemfire-specific subclass of {@link org.springframework.transaction.TransactionException}, indicating a transaction failure at commit time.
 *
 * @author Costin Leau
 */
@SuppressWarnings("serial")
public class GemfireTransactionCommitException extends TransactionException {

	public GemfireTransactionCommitException(String message, Throwable cause) {
		super(message, cause);
	}

	public GemfireTransactionCommitException(String message) {
		super(message);
	}
}
