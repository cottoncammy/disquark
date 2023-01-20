package org.example.rest.resources.channel.thread;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
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
import org.example.rest.resources.channel.Channel;

@ImmutableJson
public interface ModifyThread extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    Optional<String> name();

    Optional<Boolean> archived();

    @JsonProperty("auto_archive_duration")
    OptionalInt autoArchiveDuration();

    Optional<Boolean> locked();

    Optional<Boolean> invitable();

    @JsonProperty("rate_limit_per_user")
    OptionalInt rateLimitPerUser();

    Optional<EnumSet<Channel.Flag>> flags();

    @JsonProperty("applied_tags")
    Optional<List<Snowflake>> appliedTags();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/channels/{channel.id}"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyThread.Builder {
        protected Builder() {
        }
    }
}
