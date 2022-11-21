package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.guild.Guild;
import org.immutables.value.Value.Enclosing;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableGuildScheduledEvent.class)
public interface GuildScheduledEvent {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("guild_id")
    Snowflake guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    @JsonProperty("creator_id")
    Optional<Snowflake> creatorId();

    String name();

    Optional<String> description();

    @JsonProperty("scheduled_start_time")
    Instant scheduledStartTime();

    @JsonProperty("scheduled_end_time")
    Optional<Instant> scheduledEndTime();

    @JsonProperty("privacy_level")
    PrivacyLevel privacyLevel();

    Status status();

    @JsonProperty("entity_type")
    EntityType entityType();

    @JsonProperty("entity_id")
    Optional<Snowflake> entityId();

    @JsonProperty("entity_metadata")
    Optional<EntityMetadata> entityMetadata();

    Optional<org.example.rest.resources.User> creator();

    @JsonProperty("user_count")
    OptionalInt userCount();

    Optional<String> image();

    enum PrivacyLevel {
        GUILD_ONLY(2);

        private final int value;

        PrivacyLevel(int value) {
            this.value = value;
        }
    }

    enum EntityType {
        STAGE_INSTANCE(1),
        VOICE(2),
        EXTERNAL(3);

        private final int value;

        EntityType(int value) {
            this.value = value;
        }
    }

    enum Status {
        SCHEDULED(1),
        ACTIVE(2),
        COMPLETED(3),
        CANCELED(4);

        private final int value;

        Status(int value) {
            this.value = value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuildScheduledEvent.EntityMetadata.class)
    interface EntityMetadata {

        static Builder builder() {
            return new Builder();
        }

        Optional<String> location();

        class Builder extends ImmutableGuildScheduledEvent.EntityMetadata.Builder {
            protected Builder() {}
        }
    }

    // TODO
    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuildScheduledEvent.UserFoo.class)
    interface UserFoo {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("guild_scheduled_event_id")
        Snowflake guildScheduledEventId();

        org.example.rest.resources.User user();

        Optional<Guild.Member> member();

        class Builder extends ImmutableGuildScheduledEvent.UserFoo.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableGuildScheduledEvent.Builder {
        protected Builder() {}
    }
}
