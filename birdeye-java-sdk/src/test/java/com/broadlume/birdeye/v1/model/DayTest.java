package com.broadlume.birdeye.v1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DayTest {

    @Test
    public void fromValueTest() {
        for (Day day : Day.values())
            assertEquals(day, Day.fromValue(day.getValue()));
    }
}
