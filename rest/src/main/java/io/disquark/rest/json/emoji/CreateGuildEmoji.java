package io.disquark.rest.json.emoji;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class CreateGuildEmoji extends AbstractRequestUni<Emoji> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract String name();

    @Redacted
    @JsonSerialize(using = ImageDataSerializer.class)
    public abstract Buffer image();

    public abstract List<Snowflake> roles();

    @Override
    public void subscribe(UniSubscriber<? super Emoji> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Emoji.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/emojis"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
