package io.disquark.rest.resources.interactions.components;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.emoji.Emoji;

@ImmutableJson
@JsonDeserialize(as = ImmutableSelectOption.class)
public interface SelectOption {

    static Builder builder() {
        return new Builder();
    }

    String label();

    String value();

    Optional<String> description();

    Optional<Emoji> emoji();

    @JsonProperty("default")
    Optional<Boolean> isDefault();

    class Builder extends ImmutableSelectOption.Builder {
        protected Builder() {
        }
    }
}