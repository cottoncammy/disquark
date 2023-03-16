package io.disquark.rest.resources.partial;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.Channel;
import io.disquark.rest.resources.channel.forum.DefaultReaction;
import io.disquark.rest.resources.channel.forum.ForumTag;

@ImmutableJson
interface PartialGuildChannelJson {

    Optional<Snowflake> id();

    String name();

    Optional<Channel.Type> type();

    Optional<String> topic();

    OptionalInt bitrate();

    @JsonProperty("user_limit")
    OptionalInt userLimit();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    @JsonProperty("permission_overwrites")
    Optional<List<Channel.Overwrite>> permissionOverwrites();

    @JsonProperty("parent_id")
    Optional<Snowflake> parentId();

    Optional<Boolean> nsfw();

    @JsonProperty("rtc_region")
    Optional<String> rtcRegion();

    @JsonProperty("video_quality_mode")
    Optional<Channel.VideoQualityMode> videoQualityMode();

    @JsonProperty("default_auto_archive_duration")
    OptionalInt defaultAutoArchiveDuration();

    @JsonProperty("default_reaction_emoji")
    Optional<DefaultReaction> defaultReactionEmoji();

    @JsonProperty("available_tags")
    Optional<List<ForumTag>> availableTags();

    @JsonProperty("default_sort_order")
    Optional<Channel.SortOrderType> defaultSortOrder();
}
