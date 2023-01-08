package org.example.rest.resources.stageinstance;

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

@ImmutableJson
public interface CreateStageInstance extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("channel_id")
    Snowflake channelId();

    String topic();

    @JsonProperty("privacy_level")
    Optional<StageInstance.PrivacyLevel> privacyLevel();

    @JsonProperty("send_start_notification")
    Optional<Boolean> sendStartNotification();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/stage-instances"))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateStageInstance.Builder {
        protected Builder() {}
    }
}
