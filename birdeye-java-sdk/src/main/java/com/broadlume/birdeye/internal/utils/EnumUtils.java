package com.broadlume.birdeye.internal.utils;

import java.util.function.Function;

/**
 * Utility functions for enums
 */
public class EnumUtils {

    /**
     * Get an enum instance from the enum value
     * @param value the value to match
     * @param function a function to get a value from an enum
     * @param enums all enums to check against
     * @param <T> the enum type
     * @param <R> the value type of the enum
     * @return the matching enum
     * @throws IllegalArgumentException if no enum matches the value
     */
    public static <T, R> T enumFromValue(R value, Function<T, R> function, T[] enums) {
        T result = null;
        for (T t : enums) {
            if (function.apply(t).equals(value)) {
                result = t;
                break;
            }
        }
        if (result == null)
            throw new IllegalArgumentException("Invalid enum value " + value);
        return result;
    }
}
