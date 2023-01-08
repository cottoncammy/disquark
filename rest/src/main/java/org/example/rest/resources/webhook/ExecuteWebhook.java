package org.example.rest.resources.webhook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.MultipartRequest;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.components.Component;
import org.immutables.value.Value.Default;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@ImmutableJson
public interface ExecuteWebhook extends MultipartRequest, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake webhookId();

    @JsonIgnore
    String webhookToken();

    @Default
    @JsonIgnore
    default boolean waitForServer() {
        return false;
    }

    @JsonIgnore
    Optional<Snowflake> threadId();

    Optional<String> content();

    Optional<String> username();

    @JsonProperty("avatar_url")
    Optional<String> avatarUrl();

    Optional<Boolean> tts();

    Optional<List<Message.Embed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    Optional<List<Component>> components();

    Optional<List<Message.Attachment>> attachments();

    Optional<EnumSet<Message.Flag>> flags();

    @JsonProperty("thread_name")
    Optional<String> threadName();

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("webhook.id", webhookId().getValue(), "webhook.token", webhookToken(), "wait", waitForServer());
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

    class Builder extends ImmutableExecuteWebhook.Builder {
        protected Builder() {}
    }
}
