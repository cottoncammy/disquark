package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.permissions.Role;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface CreateGuild extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    String name();

    @Deprecated
    Optional<String> region();

    Optional<String> icon();

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
