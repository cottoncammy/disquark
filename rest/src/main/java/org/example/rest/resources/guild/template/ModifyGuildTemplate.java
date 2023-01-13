package org.example.rest.resources.guild.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.nullableoptional.NullableOptional;

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface ModifyGuildTemplate extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    String templateCode();

    Optional<String> name();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<String> description();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/templates/{template.code}"))
                .variables(variables("guild.id", guildId().getValue(), "template.code", templateCode()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableModifyGuildTemplate.Builder {
        protected Builder() {}
    }
}
