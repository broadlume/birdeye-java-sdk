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

package com.broadlume.birdeye.v1.model;

import com.broadlume.birdeye.internal.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AggregationOption {
    /** disable all aggregations */
    DISABLE(0),
    /** disable fetch business info, enable review aggregation and search business */
    REVIEW_AGG_SEARCH_BUSINESS(1),
    /** enable search business, disable fetch business and disable review aggregation */
    SEARCH_BUSINESS(3),
    /** enable all aggregations, default */
    ENABLE_ALL(4);

    private final int value;

    AggregationOption(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static AggregationOption fromValue(int value) {
        return EnumUtils.enumFromValue(value, a -> a.getValue(), AggregationOption.values());
    }
}
