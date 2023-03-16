package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateGuildBan extends AbstractRequestUni<Void> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake userId();

    @JsonProperty("delete_message_days")
    public abstract OptionalInt deleteMessageDays();

    @JsonProperty("delete_message_seconds")
    public abstract OptionalInt deleteMessageSeconds();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/guilds/{guild.id}/bans/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
