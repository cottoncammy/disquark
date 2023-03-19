package io.disquark.rest.json.scheduledevent;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.member.GuildMember;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = GuildScheduledEvent.class)
interface GuildScheduledEventJson {

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
    Optional<GuildScheduledEvent.EntityMetadata> entityMetadata();

    Optional<io.disquark.rest.json.user.User> creator();

    @JsonProperty("user_count")
    OptionalInt userCount();

    Optional<String> image();

    enum PrivacyLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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

    enum EntityType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        STAGE_INSTANCE(1),
        VOICE(2),
        EXTERNAL(3);

        private final int value;

        EntityType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum Status {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        SCHEDULED(1),
        ACTIVE(2),
        COMPLETED(3),
        CANCELED(4);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = GuildScheduledEvent.EntityMetadata.class)
    interface EntityMetadataJson {

        Optional<String> location();
    }

    @ImmutableJson
    @JsonDeserialize(as = GuildScheduledEvent.User.class)
    interface UserJson {

        @JsonProperty("guild_scheduled_event_id")
        Snowflake guildScheduledEventId();

        io.disquark.rest.json.user.User user();

        Optional<GuildMember> member();
    }
}
