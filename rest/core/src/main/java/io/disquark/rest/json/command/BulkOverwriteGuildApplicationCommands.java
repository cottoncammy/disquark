package io.disquark.rest.json.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.vertx.core.http.HttpMethod;

@ImmutableMulti
abstract class BulkOverwriteGuildApplicationCommands extends AbstractRequestMulti<ApplicationCommand> {

    public abstract Snowflake applicationId();

    public abstract Snowflake guildId();

    public abstract List<GuildApplicationCommandOverwrite> guildApplicationCommandOverwrites();

    @Override
    public void subscribe(Flow.Subscriber<? super ApplicationCommand> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().<ApplicationCommand> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/guilds/{guild.id}/commands"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue()))
                .body(guildApplicationCommandOverwrites())
                .build();
    }
}
