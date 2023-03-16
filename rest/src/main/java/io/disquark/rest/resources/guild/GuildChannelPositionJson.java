package io.disquark.rest.resources.guild;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@JsonInclude
@ImmutableJson
interface GuildChannelPositionJson {

    Snowflake id();

    OptionalInt position();

    @JsonProperty("lock_permissions")
    Optional<Boolean> lockPermissions();

    @JsonProperty("parent_id")
    Optional<Snowflake> parentId();
}
