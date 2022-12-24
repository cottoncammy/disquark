package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface GroupDmAddRecipient extends Requestable {

    static GroupDmAddRecipient create(Snowflake channelId, Snowflake userId, String accessToken, String nick) {
        return null;
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake userId();

    @JsonProperty("access_token")
    String accessToken();

    String nick();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/recipients/{user.id}"))
                .variables(Variables.variables().set("channel.id", channelId().getValueAsString()).set("user.id", userId().getValueAsString()))
                .body(this)
                .build();
    }
}
