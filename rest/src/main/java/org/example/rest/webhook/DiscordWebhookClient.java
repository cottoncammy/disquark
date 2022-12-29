package org.example.rest.webhook;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.DiscordClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.EmptyRequest;
import org.example.rest.request.Requester;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.webhook.*;
import org.example.rest.response.Response;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

public class DiscordWebhookClient<T extends Response> extends DiscordClient<T> implements WebhooksCapable {

    public static <T extends Response> Builder<T> builder(Vertx vertx) {
        return new Builder<>(requireNonNull(vertx));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordWebhookClient<T> create(Vertx vertx) {
        return (DiscordWebhookClient<T>) builder(vertx).build();
    }

    private DiscordWebhookClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    @Override
    public Uni<Webhook> modifyWebhookWithToken(ModifyWebhookWithToken modifyWebhookWithToken) {
        return requester.request(modifyWebhookWithToken.asRequest()).flatMap(res -> res.as(Webhook.class));
    }

    @Override
    public Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{webhook.id}/{webhook.token}", false, variables("webhook.id", webhookId.getValue(), "webhook.token", webhookToken)))
                .replaceWithVoid();
    }

    @Override
    public Uni<Message> executeWebhook(ExecuteWebhook executeWebhook) {
        return requester.request(executeWebhook.asRequest()).flatMap(res -> res.as(Message.class));
    }

    private Uni<Message> executeWebhook(ExecuteWebhookOptions options, String uri) {
        JsonObject json = JsonObject.of("webhook.id", options.webhookId().getValue(), "webhook.token", options.webhookToken(), "wait", options.waitForServer());
        if (options.threadId().isPresent()) {
            json.put("thread_id", options.threadId().get());
        }

        return requester.request(new EmptyRequest(HttpMethod.POST, uri, false, Variables.variables(json))).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> executeSlackCompatibleWebhook(ExecuteWebhookOptions options) {
        return executeWebhook(options, "/webhooks/{webhook.id}/{webhook.token}/slack{?thread_id,wait}");
    }

    @Override
    public Uni<Message> executeGitHubCompatibleWebhook(ExecuteWebhookOptions options) {
        return executeWebhook(options, "/webhooks/{webhook.id}/{webhook.token}/github{?thread_id,wait}");
    }

    private Uni<T> webhookMessageRequest(WebhookMessageOptions options, HttpMethod httpMethod) {
        JsonObject json = JsonObject.of("webhook.id", options.webhookId().getValue(), "webhook.token", options.webhookToken(), "message.id", options.messageId().getValue());
        if (options.threadId().isPresent()) {
            json.put("thread_id", options.threadId().get());
        }

        return requester.request(new EmptyRequest(httpMethod, "/webhooks/{webhook.id}/{webhook.token}/messages/{message.id}{?thread_id}", false, Variables.variables(json)));
    }

    @Override
    public Uni<Message> getWebhookMessage(WebhookMessageOptions options) {
        return webhookMessageRequest(options, HttpMethod.GET).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> editWebhookMessage(EditWebhookMessage editWebhookMessage) {
        return requester.request(editWebhookMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Void> deleteWebhookMessage(WebhookMessageOptions options) {
        return webhookMessageRequest(options, HttpMethod.DELETE).replaceWithVoid();
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordWebhookClient<T>> {

        protected Builder(Vertx vertx) {
            super(vertx, AccessTokenSource.DUMMY);
        }

        @Override
        public DiscordWebhookClient<T> build() {
            return new DiscordWebhookClient<>(vertx, getRequesterFactory().apply(this));
        }
    }
}
