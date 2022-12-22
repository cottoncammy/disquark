package org.example.rest.resources.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.Map;
import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
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
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableUpdateUserApplicationRoleConnections.Builder {
        protected Builder() {}
    }
}
