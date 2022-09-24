package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.immutables.ImmutableJson;

import java.util.Optional;

@ImmutableJson
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

    @Deprecated
    @JsonProperty("discoverable_disabled")
    boolean discoverableDisabled();

    @JsonProperty("guild_scheduled_event_id")
    Optional<Snowflake> guildScheduledEventId();

    enum PrivacyLevel {
        PUBLIC(1),
        GUILD_ONLY(2);

        private final int value;

        PrivacyLevel(int value) {
            this.value = value;
        }
    }

    class Builder extends ImmutableStageInstance.Builder {
        protected Builder() {}
    }
}
