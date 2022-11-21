package org.example.rest.resources.automod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.immutables.value.Value.Enclosing;

import java.util.Optional;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableAutoModerationAction.class)
public interface AutoModerationAction {

    static Builder builder() {
        return new Builder();
    }

    Type type();

    Optional<Metadata> metadata();

    enum Type {
        BLOCK_MESSAGE(1),
        SEND_ALERT_MESSAGE(2),
        TIMEOUT(3);

        private final int value;

        Type(int value) {
            this.value = value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableAutoModerationAction.Metadata.class)
    interface Metadata {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("channel_id")
        Snowflake channelId();

        @JsonProperty("duration_seconds")
        int durationSeconds();

        class Builder extends ImmutableAutoModerationAction.Metadata.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableAutoModerationAction.Builder {
        protected Builder() {}
    }
}
