package org.example.rest.resources.guild.template;

import static org.example.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.immutables.value.Value.Redacted;

@ImmutableJson
public interface CreateGuildFromGuildTemplate extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    String templateCode();

    String name();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    Optional<Buffer> icon();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/templates/{template.code}"))
                .variables(variables("template.code", templateCode()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGuildFromGuildTemplate.Builder {
        protected Builder() {
        }
    }
}
