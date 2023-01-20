package org.example.rest.resources.guild.scheduledevent;

import static org.example.rest.util.Variables.variables;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.NullableOptional;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface ModifyGuildScheduledEvent extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake guildScheduledEventId();

    @JsonProperty("channel_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Snowflake> channelId();

    @JsonProperty("entity_metadata")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<GuildScheduledEvent.EntityMetadata> entityMetadata();

    Optional<String> name();

    @JsonProperty("privacy_level")
    Optional<GuildScheduledEvent.PrivacyLevel> privacyLevel();

    @JsonProperty("scheduled_start_time")
    Optional<Instant> scheduledStartTime();

    @JsonProperty("scheduled_end_time")
    Optional<Instant> scheduledEndTime();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<String> description();

    @JsonProperty("entity_type")
    Optional<GuildScheduledEvent.EntityType> entityType();

    Optional<GuildScheduledEvent.Status> status();

    Optional<String> image();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}"))
                .variables(variables("guild.id", guildId().getValue(), "guild_scheduled_event.id", guildScheduledEventId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildScheduledEvent.Builder {
        protected Builder() {}
    }
}
