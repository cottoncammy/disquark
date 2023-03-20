package io.disquark.rest.json.invite;

import java.util.Optional;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableUni
abstract class GetInvite extends AbstractRequestUni<Invite> {

    public abstract String inviteCode();

    @Default
    public boolean withCounts() {
        return false;
    }

    @Default
    public boolean withExpiration() {
        return false;
    }

    public abstract Optional<Snowflake> guildScheduledEventId();

    @Override
    public void subscribe(UniSubscriber<? super Invite> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Invite.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("invite.code", inviteCode(), "with_counts", withCounts(), "with_expiration",
                withExpiration());
        if (guildScheduledEventId().isPresent()) {
            json.put("guild_scheduled_event_id", guildScheduledEventId().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET,
                        "/invites/{invite.code}{?with_counts,with_expiration,guild_scheduled_event_id}"))
                .variables(Variables.variables(json))
                .build();
    }
}
