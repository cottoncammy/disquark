package org.example.rest.resources.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

// TODO
@ImmutableJson
public interface CreateDm extends Requestable {

    static Builder builder() {
        return new Builder();
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

    class Builder extends ImmutableCreateDm.Builder {
        protected Builder() {}
    }
}
