package com.broadlume.birdeye.internal.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumUtilsTest {

    /**
     * Should find the correct enum matching the value
     */
    @Test
    public void enumFromValueTest() {
        assertEquals(DummyEnum.VALUE_1, EnumUtils.enumFromValue("value1", e -> e.getValue(), DummyEnum.values()));
        assertEquals(DummyEnum.VALUE_2, EnumUtils.enumFromValue("value2", e -> e.getValue(), DummyEnum.values()));
    }

    /**
     * Should fail if an unrecognized value is passed in
     */
    @Test(expected = IllegalArgumentException.class)
    public void enumFromValueNoMatchTest() {
        EnumUtils.enumFromValue("bad value", e -> e.getValue(), DummyEnum.values());
    }


    public enum DummyEnum {
        VALUE_1("value1"),
        VALUE_2("value2");

        private final String value;

        DummyEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
