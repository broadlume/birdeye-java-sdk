package com.broadlume.birdeye.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Value @Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Address.AddressBuilder.class)
public class Address {

    @Nonnull
    String address1;
    @Nullable
    String address2;
    @Nonnull
    String city;
    @Nonnull
    String state;
    @Nonnull
    String zip;
    @Nonnull
    String countryCode;
    @Nonnull
    String countryName;
    // these use a weird format
    // "latitude": 3876198440, "longitude": -10807343550 corresponds to 38.7619844,-108.0734355
    @Nullable
    Long latitude;
    @Nullable
    Long longitude;

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressBuilder {

    }
}
