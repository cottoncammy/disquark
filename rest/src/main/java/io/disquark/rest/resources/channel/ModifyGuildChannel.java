package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.forum.DefaultReaction;
import io.disquark.rest.resources.channel.forum.ForumTag;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyGuildChannel extends AbstractRequestUni<Channel> implements Auditable {

    @JsonIgnore
    public abstract Snowflake channelId();

    public abstract Optional<String> name();

    public abstract Optional<Channel.Type> type();

    public abstract OptionalInt position();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<String> topic();

    public abstract Optional<Boolean> nsfw();

    @JsonProperty("rate_limit_per_user")
    public abstract OptionalInt rateLimitPerUser();

    public abstract OptionalInt bitrate();

    @JsonProperty("user_limit")
    public abstract OptionalInt userLimit();

    @JsonProperty("permission_overwrites")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<List<Channel.Overwrite>> permissionOverwrites();

    @JsonProperty("parent_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Snowflake> parentId();

    @JsonProperty("rtc_region")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<String> rtcRegion();

    @JsonProperty("video_quality_mode")
    public abstract Optional<Channel.VideoQualityMode> videoQualityMode();

    @JsonProperty("default_auto_archive_duration")
    public abstract OptionalInt defaultAutoArchiveDuration();

    public abstract Optional<EnumSet<Channel.Flag>> flags();

    @JsonProperty("available_tags")
    public abstract Optional<List<ForumTag>> availableTags();

    @JsonProperty("default_reaction_emoji")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<DefaultReaction> defaultReactionEmoji();

    @JsonProperty("default_thread_rate_limit_per_user")
    public abstract OptionalInt defaultThreadRateLimitPerUser();

    @JsonProperty("default_sort_order")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Channel.SortOrderType> defaultSortOrder();

    @JsonProperty("default_forum_layout")
    public abstract Optional<Channel.ForumLayoutType> defaultForumLayout();

    @Override
    public void subscribe(UniSubscriber<? super Channel> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Channel.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/channels/{channel.id}"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
