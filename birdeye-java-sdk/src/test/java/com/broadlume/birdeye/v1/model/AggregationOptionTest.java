package com.broadlume.birdeye.v1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregationOptionTest {

    @Test
    public void fromValueTest() {
        assertEquals(AggregationOption.DISABLE, AggregationOption.fromValue(0));
        assertEquals(AggregationOption.REVIEW_AGG_SEARCH_BUSINESS, AggregationOption.fromValue(1));
        assertEquals(AggregationOption.SEARCH_BUSINESS, AggregationOption.fromValue(3));
        assertEquals(AggregationOption.ENABLE_ALL, AggregationOption.fromValue(4));
    }

    /**
     * Should fail if an invalid value is passed in
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidTest() {
        AggregationOption.fromValue(8);
    }
}
