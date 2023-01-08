package org.example.rest.resources.stageinstance;

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

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyStageInstance extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake channelId();

    Optional<String> topic();

    @JsonProperty("privacy_level")
    Optional<StageInstance.PrivacyLevel> privacyLevel();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/stage-instances/{channel.id}"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyStageInstance.Builder {
        protected Builder() {}
    }
}
