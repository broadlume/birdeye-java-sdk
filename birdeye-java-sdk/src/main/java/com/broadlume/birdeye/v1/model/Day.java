package com.broadlume.birdeye.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Day {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    // TODO is this a number or string? example shows string, real request uses number
    private final int value;

    Day(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static Day fromValue(int value) {
        Day day = null;
        for (Day d : Day.values()) {
            if (d.value == value) {
                day = d;
                break;
            }
        }
        if (day == null)
            throw new IllegalArgumentException("Unexpected Day value " + value);
        return day;
    }
}
