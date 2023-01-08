package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.invite.Invite;

import java.util.Optional;
import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

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
        protected Builder() {}
    }
}
