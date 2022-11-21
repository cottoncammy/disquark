package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.guild.Guild;

import java.time.Instant;
import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableGuildTemplate.class)
public interface GuildTemplate {

    static Builder builder() {
        return new Builder();
    }

    String code();

    String name();

    Optional<String> description();

    @JsonProperty("usage_count")
    int usageCount();

    @JsonProperty("creator_id")
    Snowflake creatorId();

    User creator();

    @JsonProperty("created_at")
    Instant createdAt();

    @JsonProperty("updated_at")
    Instant updatedAt();

    @JsonProperty("source_guild_id")
    Snowflake sourceGuildId();

    @JsonProperty("serialized_source_guild")
    Guild serializedSourceGuild();

    @JsonProperty("is_dirty")
    Optional<Boolean> isDirty();

    class Builder extends ImmutableGuildTemplate.Builder {
        protected Builder() {}
    }
}
