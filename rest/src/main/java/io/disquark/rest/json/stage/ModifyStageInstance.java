package io.disquark.rest.json.stage;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyStageInstance extends AbstractRequestUni<StageInstance> implements Auditable {

    @JsonIgnore
    public abstract Snowflake channelId();

    public abstract Optional<String> topic();

    @JsonProperty("privacy_level")
    public abstract Optional<StageInstanceJson.PrivacyLevel> privacyLevel();

    @Override
    public void subscribe(UniSubscriber<? super StageInstance> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(StageInstance.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/stage-instances/{channel.id}"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
