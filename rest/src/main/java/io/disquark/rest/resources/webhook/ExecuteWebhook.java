package io.disquark.rest.resources.webhook;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.MultipartRequest;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.AllowedMentions;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.partial.PartialAttachment;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableUni
abstract class ExecuteWebhook extends AbstractRequestUni<Message> implements MultipartRequest {

    @JsonIgnore
    public abstract Snowflake webhookId();

    @JsonIgnore
    public abstract String webhookToken();

    @Default
    @JsonIgnore
    public boolean waitForServer() {
        return false;
    }

    @JsonIgnore
    public abstract Optional<Snowflake> threadId();

    public abstract Optional<String> content();

    public abstract Optional<String> username();

    @JsonProperty("avatar_url")
    public abstract Optional<String> avatarUrl();

    public abstract Optional<Boolean> tts();

    public abstract Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    public abstract Optional<AllowedMentions> allowedMentions();

    public abstract Optional<List<Component>> components();

    public abstract Optional<List<PartialAttachment>> attachments();

    public abstract Optional<EnumSet<Message.Flag>> flags();

    @JsonProperty("thread_name")
    public abstract Optional<String> threadName();

    @Override
    public void subscribe(UniSubscriber<? super Message> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Message.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        JsonObject json = JsonObject.of("webhook.id", webhookId().getValue(), "webhook.token", webhookToken(), "wait",
                waitForServer());
        if (threadId().isPresent()) {
            json.put("thread_id", threadId().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/webhooks/{webhook.id}/{webhook.token}{?wait,thread_id}", false))
                .variables(Variables.variables(json))
                .body(this)
                .files(files())
                .build();
    }
}
