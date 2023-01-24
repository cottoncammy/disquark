package io.disquark.rest.resources.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

@ImmutableJson
public interface CreateDm extends Requestable {

    static CreateDm create(Snowflake recipientId) {
        return ImmutableCreateDm.create(recipientId);
    }

    @JsonProperty("recipient_id")
    Snowflake recipientId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/users/@me/channels"))
                .body(this)
                .build();
    }
}
