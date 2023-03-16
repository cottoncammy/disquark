package io.disquark.rest.resources.application.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class EditApplicationCommandPermissions extends AbstractRequestUni<GuildApplicationCommandPermissions> {

    @JsonIgnore
    public abstract Snowflake applicationId();

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake commandId();

    public abstract List<ApplicationCommand.Permissions> permissions();

    @Override
    public void subscribe(UniSubscriber<? super GuildApplicationCommandPermissions> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT,
                        "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue(),
                        "command.id", commandId().getValue()))
                .body(this)
                .build();
    }
}
