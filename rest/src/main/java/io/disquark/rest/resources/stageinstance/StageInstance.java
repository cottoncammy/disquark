package io.disquark.rest.resources.stageinstance;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ImmutableStageInstance.class)
public interface StageInstance {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("guild_id")
    Snowflake guildId();

    @JsonProperty("channel_id")
    Snowflake channelId();

    String topic();

    @JsonProperty("privacy_level")
    PrivacyLevel privacyLevel();

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

    class Builder extends ImmutableStageInstance.Builder {
        protected Builder() {
        }
    }
}
