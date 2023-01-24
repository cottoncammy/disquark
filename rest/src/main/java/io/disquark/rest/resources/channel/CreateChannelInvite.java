package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.invite.Invite;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface CreateChannelInvite extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    static CreateChannelInvite create(Snowflake channelId) {
        return ImmutableCreateChannelInvite.create(channelId);
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonProperty("max_age")
    OptionalInt maxAge();

    @JsonProperty("max_uses")
    OptionalInt maxUses();

    Optional<Boolean> temporary();

    Optional<Boolean> unique();

    @JsonProperty("target_type")
    Optional<Invite.TargetType> targetType();

    @JsonProperty("target_user_id")
    Optional<Snowflake> targetUserId();

    @JsonProperty("target_application_id")
    Optional<Snowflake> targetApplicationId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/invites"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateChannelInvite.Builder {
        protected Builder() {
        }
    }
}
