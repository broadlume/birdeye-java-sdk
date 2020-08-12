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
@JsonDeserialize(builder = Business.BusinessBuilder.class)
public class Business {

    // TODO
    // figure out which ones are actually in the API since the docs and actual requests/responses don't match
    // figure out what is actually nullable
    @Nonnull
    String name;
    @Nonnull
    String alias;
    @Nullable
    String emailId;
    @Nonnull
    String phone;
    @Nullable
    String fax;
    @Nonnull
    String websiteUrl;
    @Nonnull
    String description;
    @Nonnull
    String keywords;
    @Nonnull
    String services;
    @Nonnull
    String logoUrl;
    @Nonnull
    String coverImageUrl;
    @Nonnull
    String image1Url;
    @Nonnull
    String image2Url;
    @Nonnull
    String image3Url;
    @Nullable
    String timezone;
    @Nullable
    String countryCode;
    @Nonnull @Singular
    List<String> languages;
    @Nonnull
    String payment;
    @Nonnull @Singular
    List<HoursOfOperations> hoursOfOperations;
    @Nullable
    String specialHours;
    @Nonnull
    String isSEOEnabled;
    @Nullable
    String externalReferenceId;
    @Nullable
    String createdDate;
    int working24x7;
    @Nonnull
    String widLabel;
    @Nonnull
    String widBGColor;
    @Nonnull
    Address location;
    @Nonnull @Singular("categoryItem")
    List<String> categoryList;
    @Nullable
    String category;
    @Nonnull
    SocialProfileUrls socialProfileURLs;
    @Nullable
    Long reviewCount;
    @Nullable
    Float avgRating;
    @Nullable
    String status;
    @Nullable
    String type;
    @Nullable
    String baseUrl;

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BusinessBuilder {

    }
}
