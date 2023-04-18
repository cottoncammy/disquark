package io.disquark.rest.json.guild;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = GuildVanityUrl.class)
interface GuildVanityUrlJson {

    Optional<String> code();

    int uses();
}
