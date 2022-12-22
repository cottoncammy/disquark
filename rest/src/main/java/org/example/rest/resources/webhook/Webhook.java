package org.example.rest.resources.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.ImmutableWebhook;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.User;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.guild.Guild;

import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableWebhook.class)
public interface Webhook {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    Type type();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    Optional<User> user();

    Optional<String> name();

    Optional<String> avatar();

    Optional<String> token();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("source_guild")
    Optional<Guild> sourceGuild();

    @JsonProperty("source_channel")
    Optional<Channel> sourceChannel();

    Optional<String> url();

    enum Type {
        INCOMING(1),
        CHANNEL_FOLLOWER(2),
        APPLICATION(3);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    class Builder extends ImmutableWebhook.Builder {
        protected Builder() {}
    }
}
