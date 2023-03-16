package io.disquark.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = FollowedChannel.class)
interface FollowedChannelJson {

    @JsonProperty("channel_id")
    Snowflake channelId();

    @JsonProperty("webhook_id")
    Snowflake webhookId();
}
