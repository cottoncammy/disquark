package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.forum.DefaultReaction;
import org.example.rest.resources.channel.forum.ForumTag;
import org.example.nullableoptional.NullableOptional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyGuildChannel extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    Optional<String> name();

    Optional<Channel.Type> type();

    OptionalInt position();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<String> topic();

    Optional<Boolean> nsfw();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    OptionalInt bitrate();

    @JsonProperty("user_limit")
    OptionalInt userLimit();

    @JsonProperty("permission_overwrites")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<List<Channel.Overwrite>> permissionOverwrites();

    @JsonProperty("parent_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Snowflake> parentId();

    @JsonProperty("rtc_region")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<String> rtcRegion();

    @JsonProperty("video_quality_mode")
    Optional<Channel.VideoQualityMode> videoQualityMode();

    @JsonProperty("default_auto_archive_duration")
    OptionalInt defaultAutoArchiveDuration();

    Optional<EnumSet<Channel.Flag>> flags();

    @JsonProperty("available_tags")
    Optional<List<ForumTag>> availableTags();

    @JsonProperty("default_reaction_emoji")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<DefaultReaction> defaultReactionEmoji();

    @JsonProperty("default_thread_rate_limit_per_user")
    OptionalInt defaultThreadRateLimitPerUser();

    @JsonProperty("default_sort_order")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Channel.SortOrderType> defaultSortOrder();

    @JsonProperty("default_forum_layout")
    Optional<Channel.ForumLayoutType> defaultForumLayout();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/channels/{channel.id}"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildChannel.Builder {
        protected Builder() {}
    }

}
