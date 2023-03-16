package io.disquark.rest.resources.webhook;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Webhook.class)
interface WebhookJson {

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
    Optional<Webhook.PartialGuild> sourceGuild();

    @JsonProperty("source_channel")
    Optional<Webhook.PartialChannel> sourceChannel();

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

    @ImmutableJson
    @JsonDeserialize(as = Webhook.PartialGuild.class)
    interface PartialGuildJson {

        Snowflake id();

        String name();

        Optional<String> icon();
    }

    @ImmutableJson
    @JsonDeserialize(as = Webhook.PartialChannel.class)
    interface PartialChannelJson {

        Snowflake id();

        Optional<String> name();
    }
}
