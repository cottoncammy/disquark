package io.disquark.rest.resources.invite;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface GetInvite extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetInvite create(String inviteCode) {
        return ImmutableGetInvite.create(inviteCode);
    }

    String inviteCode();

    @Default
    default boolean withCounts() {
        return false;
    }

    @Default
    default boolean withExpiration() {
        return false;
    }

    Optional<Snowflake> guildScheduledEventId();

    @Override
    default Request asRequest() {
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

    class Builder extends ImmutableGetInvite.Builder {
        protected Builder() {
        }
    }
}
