package io.disquark.rest.json.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class EditChannelPermissions extends AbstractRequestUni<Void> implements Auditable {

    @JsonIgnore
    public abstract Snowflake channelId();

    @JsonIgnore
    public abstract Snowflake overwriteId();

    public abstract NullableOptional<EnumSet<PermissionFlag>> allow();

    public abstract NullableOptional<EnumSet<PermissionFlag>> deny();

    public abstract Channel.Overwrite.Type type();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/permissions/{overwrite.id}"))
                .variables(variables("channel.id", channelId().getValue(), "overwrite.id", overwriteId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
