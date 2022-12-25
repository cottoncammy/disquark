package org.example.rest.resources.guild.scheduledevent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.time.Instant;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface CreateGuildScheduledEvent extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    @JsonProperty("entity_metadata")
    Optional<GuildScheduledEvent.EntityMetadata> entityMetadata();

    String name();

    @JsonProperty("privacy_level")
    GuildScheduledEvent.PrivacyLevel privacyLevel();

    @JsonProperty("scheduled_start_time")
    Instant scheduledStartTime();

    @JsonProperty("scheduled_end_time")
    Optional<Instant> scheduledEndTime();

    Optional<String> description();

    @JsonProperty("entity_type")
    GuildScheduledEvent.EntityType entityType();

    Optional<String> image();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/scheduled-events"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGuildScheduledEvent.Builder {
        protected Builder() {}
    }
}
