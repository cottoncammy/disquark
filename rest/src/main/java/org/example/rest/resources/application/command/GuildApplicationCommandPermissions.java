package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

import java.util.List;

@ImmutableJson
@JsonDeserialize(as = ImmutableGuildApplicationCommandPermissions.class)
public interface GuildApplicationCommandPermissions {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("application_id")
    Snowflake applicationId();

    @JsonProperty("guild_id")
    Snowflake guildId();

    List<ApplicationCommand.Permissions> permissions();

    class Builder extends ImmutableGuildApplicationCommandPermissions.Builder {
        protected Builder() {}
    }
}
