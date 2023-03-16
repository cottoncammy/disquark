package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyCurrentUserVoiceState extends AbstractRequestUni<Void> {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonProperty("channel_id")
    public abstract Optional<Snowflake> channelId();

    public abstract Optional<Boolean> suppress();

    @JsonProperty("request_to_speak_timestamp")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Instant> requestToSpeakTimestamp();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/voice-states/@me"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .build();
    }
}
