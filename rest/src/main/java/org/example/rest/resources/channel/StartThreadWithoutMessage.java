package org.example.rest.resources.channel;

import static org.example.rest.util.Variables.variables;

import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface StartThreadWithoutMessage extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    String name();

    @JsonProperty("auto_archive_duration")
    OptionalInt autoArchiveDuration();

    Optional<Channel.Type> type();

    Optional<Boolean> invitable();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/threads"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableStartThreadWithoutMessage.Builder {
        protected Builder() {}
    }
}
