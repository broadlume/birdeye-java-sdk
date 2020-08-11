package com.broadlume.birdeye.v1.model;

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
        AggregationOption option = null;
        for (AggregationOption a : AggregationOption.values()) {
            if (a.value == value) {
                option = a;
                break;
            }
        }
        if (option == null)
            throw new IllegalArgumentException("Unexpected AggregationOption value " + value);
        return option;
    }
}
