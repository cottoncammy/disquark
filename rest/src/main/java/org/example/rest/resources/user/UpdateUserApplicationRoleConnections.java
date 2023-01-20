package org.example.rest.resources.user;

import static org.example.rest.util.Variables.variables;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

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
