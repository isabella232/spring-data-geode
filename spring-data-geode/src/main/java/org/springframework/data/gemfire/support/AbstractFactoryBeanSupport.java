/*
 * Copyright 2017-2021 the original author or authors.
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
package org.springframework.data.gemfire.support;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AbstractFactoryBeanSupport} class is an abstract Spring {@link FactoryBean} base class implementation
 * encapsulating operations common to SDG's {@link FactoryBean} implementations.
 *
 * @author John Blum
 * @see org.apache.commons.logging.Log
 * @see org.apache.commons.logging.LogFactory
 * @see org.springframework.beans.factory.BeanClassLoaderAware
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.BeanFactoryAware
 * @see org.springframework.beans.factory.BeanNameAware
 * @see org.springframework.beans.factory.FactoryBean
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class AbstractFactoryBeanSupport<T>
		implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, BeanNameAware {

	protected static final boolean DEFAULT_SINGLETON = true;

	private ClassLoader beanClassLoader;

	private BeanFactory beanFactory;

	private final Logger log;

	private String beanName;

	/**
	 * Constructs a new instance of {@link AbstractFactoryBeanSupport} initializing an object instance {@link Logger}.
	 *
	 * @see #newLog()
	 */
	protected AbstractFactoryBeanSupport() {
		this.log = newLog();
	}

	/**
	 * Constructs a new instance of {@link Logger} to log statements printed by Spring Data GemFire/Geode.
	 *
	 * @return a new instance of {@link Logger}.
	 * @see org.apache.commons.logging.LogFactory#getLog(Class)
	 * @see org.apache.commons.logging.Log
	 */
	protected Logger newLog() {
		return LoggerFactory.getLogger(getClass());
	}

	/**
	 * Sets a reference to the {@link ClassLoader} used by the Spring container to load bean {@link Class classes}.
	 *
	 * @param classLoader {@link ClassLoader} used by the Spring container to load bean {@link Class classes}.
	 * @see org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(ClassLoader)
	 * @see java.lang.ClassLoader
	 * @see java.lang.Class
	 */
	@Override
	public void setBeanClassLoader(@Nullable ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * Returns a reference to the {@link ClassLoader} used by the Spring container to load bean {@link Class classes}.
	 *
	 * @return the {@link ClassLoader} used by the Spring container to load bean {@link Class classes}.
	 * @see org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(ClassLoader)
	 * @see java.lang.ClassLoader
	 * @see java.lang.Class
	 */
	public @Nullable ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	/**
	 * Sets a reference to the Spring {@link BeanFactory} in which this {@link FactoryBean} was declared.
	 *
	 * @param beanFactory reference to the declaring Spring {@link BeanFactory}.
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(BeanFactory)
	 * @see org.springframework.beans.factory.BeanFactory
	 */
	@Override
	public void setBeanFactory(@Nullable BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * Returns a reference to the Spring {@link BeanFactory} in which this {@link FactoryBean} was declared.
	 *
	 * @return a reference to the declaring Spring {@link BeanFactory}.
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(BeanFactory)
	 * @see org.springframework.beans.factory.BeanFactory
	 */
	public @Nullable BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	/**
	 * Sets the {@link String bean name} assigned to this {@link FactoryBean} as declared in the Spring container.
	 *
	 * @param name {@link String bean name} assigned to this {@link FactoryBean} as declared in the Spring container.
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(String)
	 * @see java.lang.String
	 */
	@Override
	public void setBeanName(@Nullable String name) {
		this.beanName = name;
	}

	/**
	 * Returns the {@link String bean name} assigned to this {@link FactoryBean} as declared in the Spring container.
	 *
	 * @return the {@link String bean name} assigned to this {@link FactoryBean} as declared in the Spring container.
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(String)
	 * @see java.lang.String
	 */
	public @Nullable String getBeanName() {
		return this.beanName;
	}

	/**
	 * Returns a reference to the {@link Logger} used by this {@link FactoryBean} to log {@link String messages}.
	 *
	 * @return a reference to the {@link Logger} used by this {@link FactoryBean} to log {@link String messages}.
	 * @see org.apache.commons.logging.Log
	 */
	protected @Nullable Logger getLog() {
		return this.log;
	}

	/**
	 * Returns an {@link Optional} reference to the {@link Logger} used by this {@link FactoryBean}
	 * to log {@link String messages}.
	 *
	 * @return an {@link Optional} reference to the {@link Logger} used by this {@link FactoryBean}
	 * to log {@link String messages}.
	 * @see java.util.Optional
	 * @see org.slf4j.Logger
	 * @see #getLog()
	 */
	protected Optional<Logger> getOptionalLog() {
		return Optional.ofNullable(getLog());
	}

	/**
	 * Determines whether {@literal DEBUG} logging is enabled.
	 *
	 * @return a boolean value indicating whether {@literal DEBUG} logging is enabled.
	 * @see org.slf4j.Logger#isDebugEnabled()
	 * @see #getOptionalLog()
	 */
	public boolean isDebugLoggingEnabled() {
		return getOptionalLog().filter(Logger::isInfoEnabled).isPresent();
	}

	/**
	 * Determines whether {@literal INFO} logging is enabled.
	 *
	 * @return a boolean value indicating whether {@literal INFO} logging is enabled.
	 * @see org.slf4j.Logger#isInfoEnabled()
	 * @see #getOptionalLog()
	 */
	public boolean isInfoLoggingEnabled() {
		return getOptionalLog().filter(Logger::isInfoEnabled).isPresent();
	}

	/**
	 * Determines whether {@literal ERROR} logging is enabled.
	 *
	 * @return a boolean value indicating whether {@literal ERROR} logging is enabled.
	 * @see org.slf4j.Logger#isErrorEnabled()
	 * @see #getOptionalLog()
	 */
	public boolean isErrorLoggingEnabled() {
		return getOptionalLog().filter(Logger::isInfoEnabled).isPresent();
	}

	/**
	 * Determines whether {@literal WARN} logging is enabled.
	 *
	 * @return a boolean value indicating whether {@literal WARN} logging is enabled.
	 * @see org.slf4j.Logger#isWarnEnabled()
	 * @see #getOptionalLog()
	 */
	public boolean isWarnLoggingEnabled() {
		return getOptionalLog().filter(Logger::isInfoEnabled).isPresent();
	}

	/**
	 * Indicates that this {@link FactoryBean} produces a single bean instance.
	 *
	 * @return {@literal true} by default.
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return DEFAULT_SINGLETON;
	}

	/**
	 * Logs the {@link String message} formatted with the array of {@link Object arguments} at debug level.
	 *
	 * @param message {@link String} containing the message to log.
	 * @param args array of {@link Object arguments} used to format the {@code message}.
	 * @see #logDebug(Supplier)
	 */
	protected void logDebug(String message, Object... args) {
		logDebug(() -> String.format(message, args));
	}

	/**
	 * Logs the {@link String message} supplied by the given {@link Supplier} at debug level.
	 *
	 * @param message {@link Supplier} containing the {@link String message} and arguments to log.
	 * @see org.apache.commons.logging.Log#isDebugEnabled()
	 * @see org.apache.commons.logging.Log#debug(Object)
	 * @see #getLog()
	 */
	protected void logDebug(Supplier<String> message) {
		getOptionalLog()
			.filter(Logger::isDebugEnabled)
			.ifPresent(log -> log.debug(message.get()));
	}

	/**
	 * Logs the {@link String message} formatted with the array of {@link Object arguments} at info level.
	 *
	 * @param message {@link String} containing the message to log.
	 * @param args array of {@link Object arguments} used to format the {@code message}.
	 * @see #logInfo(Supplier)
	 */
	protected void logInfo(String message, Object... args) {
		logInfo(() -> String.format(message, args));
	}

	/**
	 * Logs the {@link String message} supplied by the given {@link Supplier} at info level.
	 *
	 * @param message {@link Supplier} containing the {@link String message} and arguments to log.
	 * @see org.apache.commons.logging.Log#isInfoEnabled()
	 * @see org.apache.commons.logging.Log#info(Object)
	 * @see #getLog()
	 */
	protected void logInfo(Supplier<String> message) {
		getOptionalLog()
			.filter(Logger::isInfoEnabled)
			.ifPresent(log -> log.info(message.get()));
	}

	/**
	 * Logs the {@link String message} formatted with the array of {@link Object arguments} at warn level.
	 *
	 * @param message {@link String} containing the message to log.
	 * @param args array of {@link Object arguments} used to format the {@code message}.
	 * @see #logWarning(Supplier)
	 */
	protected void logWarning(String message, Object... args) {
		logWarning(() -> String.format(message, args));
	}

	/**
	 * Logs the {@link String message} supplied by the given {@link Supplier} at warn level.
	 *
	 * @param message {@link Supplier} containing the {@link String message} and arguments to log.
	 * @see org.apache.commons.logging.Log#isWarnEnabled()
	 * @see org.apache.commons.logging.Log#warn(Object)
	 * @see #getLog()
	 */
	protected void logWarning(Supplier<String> message) {
		getOptionalLog()
			.filter(Logger::isWarnEnabled)
			.ifPresent(log -> log.warn(message.get()));
	}

	/**
	 * Logs the {@link String message} formatted with the array of {@link Object arguments} at error level.
	 *
	 * @param message {@link String} containing the message to log.
	 * @param args array of {@link Object arguments} used to format the {@code message}.
	 * @see #logError(Supplier)
	 */
	protected void logError(String message, Object... args) {
		logError(() -> String.format(message, args));
	}

	/**
	 * Logs the {@link String message} supplied by the given {@link Supplier} at error level.
	 *
	 * @param message {@link Supplier} containing the {@link String message} and arguments to log.
	 * @see org.apache.commons.logging.Log#isErrorEnabled()
	 * @see org.apache.commons.logging.Log#error(Object)
	 * @see #getLog()
	 */
	protected void logError(Supplier<String> message) {
		getOptionalLog()
			.filter(Logger::isErrorEnabled)
			.ifPresent(log -> log.error(message.get()));
	}
}
