package io.disquark.rest.json.channel;

import static io.disquark.rest.util.Variables.variables;

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
abstract class FollowAnnouncementChannel extends AbstractRequestUni<FollowedChannel> {

    @JsonIgnore
    public abstract Snowflake channelId();

    @JsonProperty("webhook_channel_id")
    public abstract Snowflake webhookChannelId();

    @Override
    public void subscribe(UniSubscriber<? super FollowedChannel> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(FollowedChannel.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/followers"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .build();
    }
}
