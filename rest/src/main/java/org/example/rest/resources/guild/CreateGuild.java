package org.example.rest.resources.guild;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.channel.forum.DefaultReaction;
import org.example.rest.resources.channel.forum.ForumTag;
import org.example.rest.resources.permissions.Role;
import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
public interface CreateGuild extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    String name();

    @Deprecated
    Optional<String> region();

    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    Optional<Buffer> icon();

    @JsonProperty("verification_level")
    Optional<Guild.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    Optional<Guild.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    Optional<Guild.ExplicitContentFilterLevel> explicitContentFilter();

    Optional<List<Role>> roles();

    Optional<List<PartialChannel>> channels();

    @JsonProperty("afk_channel_id")
    Optional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    OptionalInt afkTimeout();

    @JsonProperty("system_channel_id")
    Optional<Snowflake> systemChannelId();

    @JsonProperty("system_channel_flags")
    Optional<EnumSet<Guild.SystemChannelFlag>> systemChannelFlags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds"))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGuild.Builder {
        protected Builder() {}
    }

    @ImmutableJson
    interface PartialChannel {

        static Builder builder() {
            return new Builder();
        }

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

        class Builder extends ImmutableCreateGuild.PartialChannel.Builder {
            protected Builder() {}
        }
    }
}
