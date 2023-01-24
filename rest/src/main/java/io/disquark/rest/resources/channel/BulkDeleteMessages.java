package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface BulkDeleteMessages extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    List<Snowflake> messages();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/messages/bulk-delete"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableBulkDeleteMessages.Builder {
        protected Builder() {
        }
    }
}
