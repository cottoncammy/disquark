package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

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
public interface AddGuildMember extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake userId();

    @Redacted
    @JsonProperty("access_token")
    String accessToken();

    Optional<String> nick();

    Optional<List<Snowflake>> roles();

    Optional<Boolean> mute();

    Optional<Boolean> deaf();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/guilds/{guild.id}/members/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableAddGuildMember.Builder {
        protected Builder() {
        }
    }
}
