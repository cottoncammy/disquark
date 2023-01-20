package org.example.rest.resources.channel;

import static org.example.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Redacted;

@ImmutableJson
public interface GroupDmAddRecipient extends Requestable {

    static GroupDmAddRecipient create(Snowflake channelId, Snowflake userId, String accessToken, String nick) {
        return ImmutableGroupDmAddRecipient.create(channelId, userId, accessToken, nick);
    }

    @JsonIgnore
    Snowflake channelId();

    @JsonIgnore
    Snowflake userId();

    @Redacted
    @JsonProperty("access_token")
    String accessToken();

    String nick();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/channels/{channel.id}/recipients/{user.id}"))
                .variables(variables("channel.id", channelId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }
}
