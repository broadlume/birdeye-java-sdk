package com.broadlume.birdeye.v1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DayTest {

    @Test
    public void fromValueTest() {
        assertEquals(Day.MONDAY, Day.fromValue(0));
        assertEquals(Day.TUESDAY, Day.fromValue(1));
        assertEquals(Day.WEDNESDAY, Day.fromValue(2));
        assertEquals(Day.THURSDAY, Day.fromValue(3));
        assertEquals(Day.FRIDAY, Day.fromValue(4));
        assertEquals(Day.SATURDAY, Day.fromValue(5));
        assertEquals(Day.SUNDAY, Day.fromValue(6));
    }

    /**
     * Should fail if an invalid value is passed in
     */
    @Test(expected = IllegalArgumentException.class)
    public void fromValueInvalidTest() {
        Day.fromValue(8);
    }
}
