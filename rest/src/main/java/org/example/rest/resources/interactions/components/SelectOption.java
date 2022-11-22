package org.example.rest.resources.interactions.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Emoji;
import org.example.rest.immutables.ImmutableJson;

import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
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
        protected Builder() {}
    }
}
