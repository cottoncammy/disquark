package io.disquark.rest.json.command;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = GuildApplicationCommandPermissions.class)
interface GuildApplicationCommandPermissionsJson {

    Snowflake id();

    @JsonProperty("application_id")
    Snowflake applicationId();

    @JsonProperty("guild_id")
    Snowflake guildId();

    List<ApplicationCommandPermissions> permissions();
}
