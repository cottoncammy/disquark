package io.disquark.rest.json.sticker;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateGuildSticker extends AbstractRequestUni<Sticker> implements Auditable, MultipartRequest {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract String name();

    public abstract String description();

    public abstract String tags();

    @Override
    public void subscribe(UniSubscriber<? super Sticker> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Sticker.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/stickers"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .files(files())
                .build();
    }
}
