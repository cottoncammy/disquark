package io.disquark.rest.json.channel;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class GroupDmAddRecipient extends AbstractRequestUni<Void> {

    @JsonIgnore
    public abstract Snowflake channelId();

    @JsonIgnore
    public abstract Snowflake userId();

    @Redacted
    @JsonProperty("access_token")
    public abstract String accessToken();

    public abstract String nick();

    @Override
    public void subscribe(UniSubscriber<? super Void> downstream) {
        requester().request(asRequest()).replaceWithVoid().subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/recipients/{user.id}"))
                .variables(variables("channel.id", channelId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }
}
