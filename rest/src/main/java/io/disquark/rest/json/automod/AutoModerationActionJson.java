package io.disquark.rest.json.automod;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = AutoModerationAction.class)
interface AutoModerationActionJson {

    Type type();

    Optional<AutoModerationAction.Metadata> metadata();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        BLOCK_MESSAGE(1),
        SEND_ALERT_MESSAGE(2),
        TIMEOUT(3);

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
    @JsonDeserialize(as = AutoModerationAction.Metadata.class)
    interface MetadataJson {

        @JsonProperty("channel_id")
        Optional<Snowflake> channelId();

        @JsonProperty("duration_seconds")
        OptionalInt durationSeconds();

        @JsonProperty("custom_message")
        Optional<String> customMessage();
    }
}
