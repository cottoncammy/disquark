package io.disquark.rest.json.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyGuildChannelPositions extends AbstractRequestUni<Void> {

    public abstract Snowflake guildId();

    public abstract List<GuildChannelPosition> positions();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/channels"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(positions())
                .build();
    }
}
