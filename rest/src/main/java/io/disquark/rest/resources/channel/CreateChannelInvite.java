package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.invite.Invite;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateChannelInvite extends AbstractRequestUni<Invite> implements Auditable {

    @JsonIgnore
    public abstract Snowflake channelId();

    @JsonProperty("max_age")
    public abstract OptionalInt maxAge();

    @JsonProperty("max_uses")
    public abstract OptionalInt maxUses();

    public abstract Optional<Boolean> temporary();

    public abstract Optional<Boolean> unique();

    @JsonProperty("target_type")
    public abstract Optional<Invite.TargetType> targetType();

    @JsonProperty("target_user_id")
    public abstract Optional<Snowflake> targetUserId();

    @JsonProperty("target_application_id")
    public abstract Optional<Snowflake> targetApplicationId();

    @Override
    public void subscribe(UniSubscriber<? super Invite> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Invite.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/invites"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
