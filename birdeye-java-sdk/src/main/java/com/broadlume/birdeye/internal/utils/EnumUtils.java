/*
 * Copyright 2020, birdeye-java-sdk Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

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

    private EnumUtils() { }
}
