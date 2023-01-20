package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ImmutableFollowedChannel.class)
public interface FollowedChannel {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("channel_id")
    Snowflake channelId();

    @JsonProperty("webhook_id")
    Snowflake webhookId();

    class Builder extends ImmutableFollowedChannel.Builder {
        protected Builder() {
        }
    }
}
