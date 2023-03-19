package io.disquark.rest.json.partial;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
interface PartialAttachmentJson {

    Snowflake id();

    Optional<String> filename();

    Optional<String> description();

    @JsonProperty("content_type")
    Optional<String> contentType();

    OptionalInt size();

    Optional<String> url();

    @JsonProperty("proxy_url")
    Optional<String> proxyUrl();

    OptionalInt height();

    OptionalInt width();

    Optional<Boolean> ephemeral();
}
