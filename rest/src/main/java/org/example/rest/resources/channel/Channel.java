package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@JsonDeserialize(as = ImmutableChannel.class)
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

    @JsonProperty("available_tags")
    Optional<List<ForumTag>> availableTags();

    @JsonProperty("applied_tags")
    Optional<List<Snowflake>> appliedTags();

    @JsonProperty("default_reaction_emoji")
    Optional<DefaultReaction> defaultReactionEmoji();

    @JsonProperty("default_thread_rate_limit_per_user")
    OptionalInt defaultThreadRateLimitPerUser();

    @JsonProperty("default_sort_order")
    Optional<SortOrderType> defaultSortOrder();

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

        public static Type create(int value) {
            switch (value) {
                case 0:
                    return GUILD_TEXT;
                case 1:
                    return DM;
                case 2:
                    return GUILD_VOICE;
                case 3:
                    return GROUP_DM;
                case 4:
                    return GUILD_CATEGORY;
                case 5:
                    return GUILD_ANNOUNCEMENT;
                case 10:
                    return ANNOUNCEMENT_THREAD;
                case 11:
                    return PUBLIC_THREAD;
                case 12:
                    return PRIVATE_THREAD;
                case 13:
                    return GUILD_STAGE_VOICE;
                case 14:
                    return GUILD_DIRECTORY;
                case 15:
                    return GUILD_FORUM;
                default:
                    throw new IllegalArgumentException();
            }
        }

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum VideoQualityMode {
        AUTO(1),
        FULL(2);

        private final int value;

        public static VideoQualityMode create(int value) {
            if (value == 1) {
                return AUTO;
            } else if (value == 2) {
                return FULL;
            } else {
                throw new IllegalArgumentException();
            }
        }

        VideoQualityMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Flag {
        PINNED(1),
        REQUIRE_TAG(4);

        private final int value;

        public static Flag create(int value) {
            if (value == 1) {
                return PINNED;
            } else if (value == 4) {
                return REQUIRE_TAG;
            } else {
                throw new IllegalArgumentException();
            }
        }

        Flag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum SortOrderType {
        LATEST_ACTIVITY(0),
        CREATION_DATE(1);

        private final int value;

        public static SortOrderType create(int value) {
            if (value == 0) {
                return LATEST_ACTIVITY;
            } else if (value == 1) {
                return CREATION_DATE;
            } else {
                throw new IllegalArgumentException();
            }
        }

        SortOrderType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableChannel.Overwrite.class)
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

            public static Type create(int value) {
                if (value == 0) {
                    return ROLE;
                } else if (value == 1) {
                    return MEMBER;
                } else {
                    throw new IllegalArgumentException();
                }
            }

            Type(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        class Builder extends ImmutableChannel.Overwrite.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableChannel.Mention.class)
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
