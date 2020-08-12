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
