package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.OptionalInt;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface CreateGuildBan extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake userId();

    @JsonProperty("delete_message_days")
    OptionalInt deleteMessageDays();

    @JsonProperty("delete_message_seconds")
    OptionalInt deleteMessageSeconds();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/guilds/{guild.id}/bans/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateGuildBan.Builder {
        protected Builder() {}
    }
}
