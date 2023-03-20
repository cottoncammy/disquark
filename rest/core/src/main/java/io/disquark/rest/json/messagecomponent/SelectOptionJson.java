package io.disquark.rest.json.messagecomponent;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.emoji.Emoji;

@ImmutableJson
@JsonDeserialize(as = SelectOption.class)
interface SelectOptionJson {

    String label();

    String value();

    Optional<String> description();

    Optional<Emoji> emoji();

    @JsonProperty("default")
    Optional<Boolean> isDefault();
}
