package org.example.rest.resources.channel;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.NullableOptional;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

@ImmutableJson
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
public interface EditChannelPermissions extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake overwriteId();

    NullableOptional<EnumSet<PermissionFlag>> allow();

    NullableOptional<EnumSet<PermissionFlag>> deny();

    Channel.Overwrite.Type type();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/permissions/{overwrite.id}"))
                .variables(variables("channel.id", channelId().getValue(), "overwrite.id", overwriteId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableEditChannelPermissions.Builder {
        protected Builder() {
        }
    }
}
