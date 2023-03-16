package io.disquark.rest.resources.guild.template;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.guild.Guild;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class CreateGuildFromGuildTemplate extends AbstractRequestUni<Guild> {

    @JsonIgnore
    public abstract String templateCode();

    public abstract String name();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract Optional<Buffer> icon();

    @Override
    public void subscribe(UniSubscriber<? super Guild> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Guild.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/templates/{template.code}"))
                .variables(variables("template.code", templateCode()))
                .body(this)
                .build();
    }
}
