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

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableMulti
abstract class BulkOverwriteGlobalApplicationCommands extends AbstractRequestMulti<ApplicationCommand> {

    public abstract Snowflake applicationId();

    public abstract List<GlobalApplicationCommandOverwrite> overwrites();

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
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/commands"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(overwrites())
                .build();
    }
}
