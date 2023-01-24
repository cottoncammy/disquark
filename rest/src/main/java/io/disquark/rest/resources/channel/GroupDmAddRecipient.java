package io.disquark.rest.resources.channel;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

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
