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
