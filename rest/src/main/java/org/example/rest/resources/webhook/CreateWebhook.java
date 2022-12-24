package org.example.rest.resources.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface CreateWebhook extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    String name();

    Optional<String> avatar();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/webhooks"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateWebhook.Builder {
        protected Builder() {}
    }
}
