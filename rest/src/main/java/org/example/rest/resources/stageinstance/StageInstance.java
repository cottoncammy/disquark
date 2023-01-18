package org.example.rest.resources.stageinstance;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

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
        protected Builder() {}
    }
}
