package org.example.rest.resources.emoji;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface CreateGuildEmoji extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    String name();

    String image();

    List<Snowflake> roles();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/emojis"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateGuildEmoji.Builder {
        protected Builder() {}
    }
}
