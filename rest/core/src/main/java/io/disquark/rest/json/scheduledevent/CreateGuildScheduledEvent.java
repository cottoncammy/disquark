package io.disquark.rest.json.scheduledevent;

import static io.disquark.rest.util.Variables.variables;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class CreateGuildScheduledEvent extends AbstractRequestUni<GuildScheduledEvent> {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonProperty("channel_id")
    public abstract Optional<Snowflake> channelId();

    @JsonProperty("entity_metadata")
    public abstract Optional<GuildScheduledEvent.EntityMetadata> entityMetadata();

    public abstract String name();

    @JsonProperty("privacy_level")
    public abstract GuildScheduledEventJson.PrivacyLevel privacyLevel();

    @JsonProperty("scheduled_start_time")
    public abstract Instant scheduledStartTime();

    @JsonProperty("scheduled_end_time")
    public abstract Optional<Instant> scheduledEndTime();

    public abstract Optional<String> description();

    @JsonProperty("entity_type")
    public abstract GuildScheduledEventJson.EntityType entityType();

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
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/scheduled-events"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .build();
    }
}
