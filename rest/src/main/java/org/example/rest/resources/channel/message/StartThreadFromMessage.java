package org.example.rest.resources.channel.message;

import static org.example.rest.util.Variables.variables;

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
public interface StartThreadFromMessage extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake messageId();

    String name();

    @JsonProperty("auto_archive_duration")
    OptionalInt autoArchiveDuration();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/messages/{message.id}/threads"))
                .variables(variables("channel.id", channelId().getValue(), "message.id", messageId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableStartThreadFromMessage.Builder {
        protected Builder() {}
    }
}
