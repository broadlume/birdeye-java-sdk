package com.broadlume.birdeye.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Value @Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = HoursOfOperations.HoursOfOperationsBuilder.class)
public class HoursOfOperations {

    @Nonnull
    Day day;
    // start and end are null if closed
    @Nullable
    String startHour;
    @Nullable
    String endHour;
    int isOpen;
    @Nullable
    String comment;
    @Nonnull @Singular
    List<WorkingHours> workingHours;

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HoursOfOperationsBuilder {

    }
}
