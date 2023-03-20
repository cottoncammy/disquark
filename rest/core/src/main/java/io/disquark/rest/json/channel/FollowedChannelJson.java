package io.disquark.rest.json.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = FollowedChannel.class)
interface FollowedChannelJson {

    @JsonProperty("channel_id")
    Snowflake channelId();

    @JsonProperty("webhook_id")
    Snowflake webhookId();
}
