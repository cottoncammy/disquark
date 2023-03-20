package io.disquark.rest.json.channel;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.FlagEnum;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.forum.DefaultReaction;
import io.disquark.rest.json.forum.ForumLayoutType;
import io.disquark.rest.json.forum.ForumTag;
import io.disquark.rest.json.forum.SortOrderType;
import io.disquark.rest.json.thread.ThreadMember;
import io.disquark.rest.json.thread.ThreadMetadata;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Channel.class)
interface ChannelJson {

    Snowflake id();

    Type type();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    OptionalInt position();

    @JsonProperty("permission_overwrites")
    Optional<List<Channel.Overwrite>> permissionOverwrites();

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

    Optional<EnumSet<PermissionFlag>> permissions();

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

    @JsonProperty("default_forum_layout")
    Optional<ForumLayoutType> defaultForumLayout();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum VideoQualityMode {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        AUTO(1),
        FULL(2);

        private final int value;

        VideoQualityMode(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum Flag implements FlagEnum {
        PINNED(1),
        REQUIRE_TAG(4);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Channel.Overwrite.class)
    interface OverwriteJson {

        Snowflake id();

        Type type();

        Optional<EnumSet<PermissionFlag>> allow();

        Optional<EnumSet<PermissionFlag>> deny();

        enum Type {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
            ROLE(0),
            MEMBER(1);

            private final int value;

            Type(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Channel.Mention.class)
    interface MentionJson {

        Snowflake id();

        @JsonProperty("guild_id")
        Snowflake guildId();

        Type type();

        String name();
    }
}
