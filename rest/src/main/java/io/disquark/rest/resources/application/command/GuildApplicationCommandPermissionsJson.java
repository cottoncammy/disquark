package io.disquark.rest.resources.application.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = GuildApplicationCommandPermissions.class)
interface GuildApplicationCommandPermissionsJson {

    Snowflake id();

    @JsonProperty("application_id")
    Snowflake applicationId();

    @JsonProperty("guild_id")
    Snowflake guildId();

    List<ApplicationCommand.Permissions> permissions();
}
