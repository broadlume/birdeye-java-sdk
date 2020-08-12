package com.broadlume.birdeye.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.annotation.Nullable;

@Value @Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = SocialProfileUrls.SocialProfileUrlsBuilder.class)
public class SocialProfileUrls {

    @Nullable
    String googleUrl;
    @Nullable
    String facebookUrl;
    @Nullable
    String twitterUrl;
    @Nullable
    String linkedinUrl;
    @Nullable
    String youtubeUrl;

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SocialProfileUrlsBuilder {

    }
}
