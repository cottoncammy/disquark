package io.disquark.rest.resources.guild.scheduledevent;

import static io.disquark.rest.util.Variables.variables;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class ModifyGuildScheduledEvent extends AbstractRequestUni<GuildScheduledEvent> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake guildScheduledEventId();

    @JsonProperty("channel_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Snowflake> channelId();

    @JsonProperty("entity_metadata")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<GuildScheduledEvent.EntityMetadata> entityMetadata();

    public abstract Optional<String> name();

    @JsonProperty("privacy_level")
    public abstract Optional<GuildScheduledEvent.PrivacyLevel> privacyLevel();

    @JsonProperty("scheduled_start_time")
    public abstract Optional<Instant> scheduledStartTime();

    @JsonProperty("scheduled_end_time")
    public abstract Optional<Instant> scheduledEndTime();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<String> description();

    @JsonProperty("entity_type")
    public abstract Optional<GuildScheduledEvent.EntityType> entityType();

    public abstract Optional<GuildScheduledEvent.Status> status();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract Optional<Buffer> image();

    @Override
    public void subscribe(UniSubscriber<? super GuildScheduledEvent> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}"))
                .variables(variables("guild.id", guildId().getValue(), "guild_scheduled_event.id",
                        guildScheduledEventId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
