package org.example.rest.resources.guild.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;

import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface CreateGuildFromGuildTemplate extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    String templateCode();

    String name();

    Optional<String> icon();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/templates/{template.code}"))
                .variables(variables("template.code", templateCode()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGuildFromGuildTemplate.Builder {
        protected Builder() {}
    }
}
