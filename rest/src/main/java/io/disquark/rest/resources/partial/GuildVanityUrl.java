package io.disquark.rest.resources.partial;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableGuildVanityUrl.class)
public interface GuildVanityUrl {

    static Builder builder() {
        return new Builder();
    }

    Optional<String> code();

    OptionalInt uses();

    class Builder extends ImmutableGuildVanityUrl.Builder {
        protected Builder() {
        }
    }
}
