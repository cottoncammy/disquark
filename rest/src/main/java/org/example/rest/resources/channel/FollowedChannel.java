package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;

@ImmutableJson
public interface FollowedChannel {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("channel_id")
    Snowflake channelId();

    @JsonProperty("webhook_id")
    Snowflake webhookId();

    class Builder extends ImmutableFollowedChannel.Builder {
        protected Builder() {}
    }
}
