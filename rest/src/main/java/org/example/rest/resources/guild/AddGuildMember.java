package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface AddGuildMember extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake userId();

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
        protected Builder() {}
    }
}
