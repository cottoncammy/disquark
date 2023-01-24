package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface FollowAnnouncementChannel extends Requestable {

    static FollowAnnouncementChannel create(Snowflake channelId, Snowflake webhookChannelId) {
        return ImmutableFollowAnnouncementChannel.create(channelId, webhookChannelId);
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonProperty("webhook_channel_id")
    Snowflake webhookChannelId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/followers"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .build();
    }
}
