package io.disquark.rest.json.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.forum.DefaultReaction;
import io.disquark.rest.json.forum.ForumTag;
import io.disquark.rest.json.forum.SortOrderType;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateGuildChannel extends AbstractRequestUni<Channel> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract String name();

    public abstract Optional<Channel.Type> type();

    public abstract Optional<String> topic();

    public abstract OptionalInt bitrate();

    @JsonProperty("user_limit")
    public abstract OptionalInt userLimit();

    @JsonProperty("rate_limit_per_user")
    public abstract OptionalInt rateLimitPerUser();

    public abstract OptionalInt position();

    @JsonProperty("permission_overwrites")
    public abstract Optional<List<Channel.Overwrite>> permissionOverwrites();

    @JsonProperty("parent_id")
    public abstract Optional<Snowflake> parentId();

    public abstract Optional<Boolean> nsfw();

    @JsonProperty("rtc_region")
    public abstract Optional<String> rtcRegion();

    @JsonProperty("video_quality_mode")
    public abstract Optional<Channel.VideoQualityMode> videoQualityMode();

    @JsonProperty("default_auto_archive_duration")
    public abstract OptionalInt defaultAutoArchiveDuration();

    @JsonProperty("default_reaction_emoji")
    public abstract Optional<DefaultReaction> defaultReactionEmoji();

    @JsonProperty("available_tags")
    public abstract Optional<List<ForumTag>> availableTags();

    @JsonProperty("default_sort_order")
    public abstract Optional<SortOrderType> defaultSortOrder();

    @Override
    public void subscribe(UniSubscriber<? super Channel> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Channel.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/channels"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
