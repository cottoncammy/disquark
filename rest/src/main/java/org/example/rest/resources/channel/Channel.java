package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.User;
import org.immutables.value.Value.Enclosing;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
public interface Channel {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    Type type();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    OptionalInt position();

    @JsonProperty("permission_overwrites")
    Optional<List<Overwrite>> permissionOverwrites();

    Optional<String> name();

    Optional<String> topic();

    Optional<Boolean> nsfw();

    @JsonProperty("last_message_id")
    Optional<Snowflake> lastMessageId();

    OptionalInt bitrate();

    @JsonProperty("user_limit")
    OptionalInt userLimit();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    Optional<List<User>> recipients();

    Optional<String> icon();

    @JsonProperty("owner_id")
    Optional<Snowflake> ownerId();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("parent_id")
    Optional<Snowflake> parentId();

    @JsonProperty("last_pin_timestamp")
    Optional<Instant> lastPinTimestamp();

    @JsonProperty("rtc_region")
    Optional<String> rtcRegion();

    @JsonProperty("video_quality_mode")
    Optional<VideoQualityMode> videoQualityMode();

    @JsonProperty("message_count")
    OptionalInt messageCount();

    @JsonProperty("member_count")
    OptionalInt memberCount();

    @JsonProperty("thread_metadata")
    Optional<ThreadMetadata> threadMetadata();

    Optional<ThreadMember> member();

    @JsonProperty("default_auto_archive_duration")
    OptionalInt defaultAutoArchiveDuration();

    Optional<String> permissions();

    Optional<EnumSet<Flag>> flags();

    @JsonProperty("total_message_sent")
    OptionalInt totalMessageSent();

    enum Type {
        GUILD_TEXT(0),
        DM(1),
        GUILD_VOICE(2),
        GROUP_DM(3),
        GUILD_CATEGORY(4),
        GUILD_ANNOUNCEMENT(5),
        ANNOUNCEMENT_THREAD(10),
        PUBLIC_THREAD(11),
        PRIVATE_THREAD(12),
        GUILD_STAGE_VOICE(13),
        GUILD_DIRECTORY(14),
        GUILD_FORUM(15);

        private final int value;

        Type(int value) {
            this.value = value;
        }
    }

    enum VideoQualityMode {
        AUTO(1),
        FULL(2);

        private final int value;

        VideoQualityMode(int value) {
            this.value = value;
        }
    }

    enum Flag {
        PINNED(1);

        private final int value;

        Flag(int value) {
            this.value = value;
        }
    }

    @ImmutableJson
    interface Overwrite {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        Type type();

        String allow();

        String deny();

        enum Type {
            ROLE(0),
            MEMBER(1);

            private final int value;

            Type(int value) {
                this.value = value;
            }
        }

        class Builder extends ImmutableChannel.Overwrite.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    interface Mention {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        @JsonProperty("guild_id")
        Snowflake guildId();

        Type type();

        String name();

        class Builder extends ImmutableChannel.Mention.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableChannel.Builder {
        protected Builder() {}
    }
}
