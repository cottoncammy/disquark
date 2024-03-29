package io.disquark.rest.json.guild;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.role.Role;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

@ImmutableUni
abstract class CreateGuild extends AbstractRequestUni<Guild> {

    public abstract String name();

    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract Optional<Buffer> icon();

    @JsonProperty("verification_level")
    public abstract Optional<Guild.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    public abstract Optional<Guild.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    public abstract Optional<Guild.ExplicitContentFilterLevel> explicitContentFilter();

    public abstract Optional<List<Role>> roles();

    public abstract Optional<List<PartialGuildChannel>> channels();

    @JsonProperty("afk_channel_id")
    public abstract Optional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    public abstract OptionalInt afkTimeout();

    @JsonProperty("system_channel_id")
    public abstract Optional<Snowflake> systemChannelId();

    @JsonProperty("system_channel_flags")
    public abstract Optional<EnumSet<Guild.SystemChannelFlag>> systemChannelFlags();

    @Override
    public void subscribe(UniSubscriber<? super Guild> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Guild.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds"))
                .body(this)
                .build();
    }
}
