package io.disquark.rest.resources.guild;

import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@ImmutableJson
interface GuildRolePositionJson {

    Snowflake id();

    @JsonInclude
    OptionalInt position();
}
