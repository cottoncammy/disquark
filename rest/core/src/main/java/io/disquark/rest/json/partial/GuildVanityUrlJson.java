package io.disquark.rest.json.partial;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = GuildVanityUrl.class)
interface GuildVanityUrlJson {

    Optional<String> code();

    OptionalInt uses();
}
