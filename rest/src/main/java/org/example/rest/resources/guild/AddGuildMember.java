package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;
import java.util.Optional;

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
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("user.id", userId().getValueAsString()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableAddGuildMember.Builder {
        protected Builder() {}
    }
}
