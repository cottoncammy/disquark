package io.disquark.rest.json.voice;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyUserVoiceState extends AbstractRequestUni<Void> {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake userId();

    @JsonProperty("channel_id")
    public abstract Snowflake channelId();

    public abstract Optional<Boolean> suppress();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/voice-states/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }
}
