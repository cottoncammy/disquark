package org.example.rest.resources.channel;

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
import org.example.rest.resources.permissions.PermissionFlag;

import java.util.EnumSet;
import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface EditChannelPermissions extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake overwriteId();

    Optional<EnumSet<PermissionFlag>> allow();

    Optional<EnumSet<PermissionFlag>> deny();

    Channel.Overwrite.Type type();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/permissions/{overwrite.id}"))
                .variables(Variables.variables().set("channel.id", channelId().getValueAsString()).set("overwrite.id", overwriteId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableEditChannelPermissions.Builder {
        protected Builder() {}
    }
}
