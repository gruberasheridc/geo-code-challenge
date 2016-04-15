package com.geo.infra.util;

public class ValidationUtils {
	
	/**
	 * Checks that the given object is not {@code null} .
	 * @param object the object to check.
	 * @param message the exception message to use if the check fails.
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void notNull(final Object object, final String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Checks that the given object is {@code null} .
	 * @param object the object to check.
	 * @param message the exception message to use if the check fails.
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static void isNull(final Object object, final String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}
	
}
