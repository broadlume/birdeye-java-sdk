package com.broadlume.birdeye.v1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregationOptionTest {

    @Test
    public void fromValueTest() {
        for (AggregationOption option : AggregationOption.values())
            assertEquals(option, AggregationOption.fromValue(option.getValue()));
    }
}
