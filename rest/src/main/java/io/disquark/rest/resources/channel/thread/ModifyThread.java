package io.disquark.rest.resources.channel.thread;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
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
import io.disquark.rest.resources.channel.Channel;
import io.vertx.core.http.HttpMethod;

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
