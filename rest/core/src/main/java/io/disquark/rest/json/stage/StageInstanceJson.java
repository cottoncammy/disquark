package io.disquark.rest.json.stage;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = StageInstance.class)
interface StageInstanceJson {

    Snowflake id();

    @JsonProperty("guild_id")
    Snowflake guildId();

    @JsonProperty("channel_id")
    Snowflake channelId();

    String topic();

    @JsonProperty("privacy_level")
    StageInstance.PrivacyLevel privacyLevel();

    @JsonProperty("guild_scheduled_event_id")
    Optional<Snowflake> guildScheduledEventId();

    enum PrivacyLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        PUBLIC(1),
        GUILD_ONLY(2);

        private final int value;

        PrivacyLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
