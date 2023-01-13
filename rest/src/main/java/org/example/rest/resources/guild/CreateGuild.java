package org.example.rest.resources.guild;

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
import org.example.rest.resources.permissions.Role;
import org.example.nullableoptional.NullableOptional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
public interface CreateGuild extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    String name();

    @Deprecated
    Optional<String> region();

    @JsonSerialize(using = ImageDataSerializer.class)
    Optional<Buffer> icon();

    @JsonProperty("verification_level")
    Optional<Guild.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    Optional<Guild.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    Optional<Guild.ExplicitContentFilterLevel> explicitContentFilter();

    Optional<List<Role>> roles();

    Optional<List<Channel>> channels();

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
}
