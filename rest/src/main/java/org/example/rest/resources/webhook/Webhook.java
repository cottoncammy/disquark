package org.example.rest.resources.webhook;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.User;
import org.immutables.value.Value.Enclosing;

@Enclosing
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
    Optional<PartialGuild> sourceGuild();

    @JsonProperty("source_channel")
    Optional<PartialChannel> sourceChannel();

    Optional<String> url();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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

    @ImmutableJson
    @JsonDeserialize(as = ImmutableWebhook.PartialGuild.class)
    interface PartialGuild {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String name();

        Optional<String> icon();

        class Builder extends ImmutableWebhook.PartialGuild.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableWebhook.PartialChannel.class)
    interface PartialChannel {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        Optional<String> name();

        class Builder extends ImmutableWebhook.PartialChannel.Builder {
            protected Builder() {}
        }
    }
}
