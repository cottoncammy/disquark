package io.disquark.rest.resources.user;

import static io.disquark.rest.util.Variables.variables;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class UpdateUserApplicationRoleConnections extends AbstractRequestUni<User.ApplicationRoleConnection> {

    @JsonIgnore
    public abstract Snowflake applicationId();

    @JsonProperty("platform_name")
    public abstract Optional<String> platformName();

    @JsonProperty("platform_username")
    public abstract Optional<String> platformUsername();

    public abstract Optional<Map<String, String>> metadata();

    @Override
    public void subscribe(UniSubscriber<? super User.ApplicationRoleConnection> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(User.ApplicationRoleConnection.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/users/@me/applications/{application.id}/role-connection"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(this)
                .build();
    }
}
