package org.example.rest.resources.guild.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyGuildTemplate extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    String templateCode();

    Optional<String> name();

    Optional<String> description();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/templates/{template.code}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("template.code", templateCode()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableModifyGuildTemplate.Builder {
        protected Builder() {}
    }
}
