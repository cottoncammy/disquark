package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface FollowAnnouncementChannel extends Requestable {

    static FollowAnnouncementChannel create(Snowflake channelId, Snowflake webhookChannelId) {
        // TODO immutables constructor method
        return null;
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonProperty("webhook_channel_id")
    Snowflake webhookChannelId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/followers"))
                .variables(Variables.variables().set("channel.id", channelId().getValueAsString()))
                .body(this)
                .build();
    }
}
