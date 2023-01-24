package io.disquark.rest.resources.user;

import static io.disquark.rest.util.Variables.variables;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface UpdateUserApplicationRoleConnections extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonProperty("platform_name")
    Optional<String> platformName();

    @JsonProperty("platform_username")
    Optional<String> platformUsername();

    Optional<Map<String, String>> metadata();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/users/@me/applications/{application.id}/role-connection"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableUpdateUserApplicationRoleConnections.Builder {
        protected Builder() {
        }
    }
}
