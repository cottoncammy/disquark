package io.disquark.rest.resources.stageinstance;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateStageInstance extends AbstractRequestUni<StageInstance> implements Auditable {

    @JsonProperty("channel_id")
    public abstract Snowflake channelId();

    public abstract String topic();

    @JsonProperty("privacy_level")
    public abstract Optional<StageInstance.PrivacyLevel> privacyLevel();

    @JsonProperty("send_start_notification")
    public abstract Optional<Boolean> sendStartNotification();

    @Override
    public void subscribe(UniSubscriber<? super StageInstance> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(StageInstance.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/stage-instances"))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
