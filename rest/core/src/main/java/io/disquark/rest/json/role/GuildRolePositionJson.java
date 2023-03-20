package io.disquark.rest.json.role;

import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
interface GuildRolePositionJson {

    Snowflake id();

    @JsonInclude
    OptionalInt position();
}
