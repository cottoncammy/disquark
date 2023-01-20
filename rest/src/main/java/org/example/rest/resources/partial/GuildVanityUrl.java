package org.example.rest.resources.partial;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableGuildVanityUrl.class)
public interface GuildVanityUrl {

    static Builder builder() {
        return new Builder();
    }

    Optional<String> code();

    OptionalInt uses();

    class Builder extends ImmutableGuildVanityUrl.Builder {
        protected Builder() {}
    }
}
