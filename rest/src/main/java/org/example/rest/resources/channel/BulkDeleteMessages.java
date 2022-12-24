package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface BulkDeleteMessages extends Auditable, Requestable {

    static BulkDeleteMessages create(Snowflake channelId, List<Snowflake> messages, String auditLogReason) {
        return null;
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
}
