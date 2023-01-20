package org.example.rest.resources.guild;

import static org.example.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.NullableOptional;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface ModifyGuildWidget extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    Optional<Boolean> enabled();

    @JsonProperty("channel_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Snowflake> channelId();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/widget"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildWidget.Builder {
        protected Builder() {
        }
    }
}
