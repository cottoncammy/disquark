package org.example.rest.resources.invite;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableStyle;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Default;

import java.util.Optional;

@Immutable
@ImmutableStyle
public interface GetInvite extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static GetInvite create(String inviteCode) {
        return null;
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
        JsonObject json = JsonObject.of("invite.code", inviteCode(), "with_counts", withCounts(), "with_expiration", withExpiration());
        if (guildScheduledEventId().isPresent()) {
            json.put("guild_scheduled_event_id", guildScheduledEventId().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/invites/{invite.code}{?with_counts,with_expiration,guild_scheduled_event_id}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableGetInvite.Builder {
        protected Builder() {}
    }
}
