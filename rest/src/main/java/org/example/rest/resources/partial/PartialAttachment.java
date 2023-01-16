package org.example.rest.resources.partial;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
public interface PartialAttachment {

    static Builder builder() {
        return new Builder();
    }

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

    class Builder extends ImmutablePartialAttachment.Builder {
        protected Builder() {}
    }
}
