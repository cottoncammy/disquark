package io.disquark.rest.webhook;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import io.disquark.rest.DiscordClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.webhook.DeleteWebhookMessageUni;
import io.disquark.rest.json.webhook.EditWebhookMessageUni;
import io.disquark.rest.json.webhook.ExecuteWebhookUni;
import io.disquark.rest.json.webhook.GetWebhookMessageUni;
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni;
import io.disquark.rest.json.webhook.Webhook;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.response.Response;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;

public class DiscordWebhookClient<T extends Response> extends DiscordClient<T> implements WebhooksCapable {

    public static <T extends Response> Builder<T> builder(Vertx vertx) {
        return new Builder<>(requireNonNull(vertx, "vertx"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordWebhookClient<T> create(Vertx vertx) {
        return (DiscordWebhookClient<T>) builder(vertx).build();
    }

    private DiscordWebhookClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    @Override
    public Uni<Webhook> getWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return deferredUni(() -> requester.request(new EmptyRequest("/webhooks/{webhook.id}/{webhook.token}", false,
                variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue(),
                        "webhook.token", requireNonNull(webhookToken, "webhookToken")))))
                .flatMap(res -> res.as(Webhook.class));
    }

    @Override
    public ModifyWebhookWithTokenUni modifyWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return (ModifyWebhookWithTokenUni) deferredUni(() -> new ModifyWebhookWithTokenUni(requester, webhookId, webhookToken));
    }

    @Override
    public Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return deferredUni(
                () -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{webhook.id}/{webhook.token}", false,
                        variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue(),
                                "webhook.token", requireNonNull(webhookToken, "webhookToken")))))
                .replaceWithVoid();
    }

    @Override
    public ExecuteWebhookUni executeWebhook(Snowflake webhookId, String webhookToken) {
        return (ExecuteWebhookUni) deferredUni(() -> new ExecuteWebhookUni(requester, webhookId, webhookToken));
    }

    @Override
    public GetWebhookMessageUni getWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return (GetWebhookMessageUni) deferredUni(
                () -> new GetWebhookMessageUni(requester, webhookId, webhookToken, messageId));
    }

    @Override
    public EditWebhookMessageUni editWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return (EditWebhookMessageUni) deferredUni(
                () -> new EditWebhookMessageUni(requester, webhookId, webhookToken, messageId));
    }

    @Override
    public DeleteWebhookMessageUni deleteWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return (DeleteWebhookMessageUni) deferredUni(
                () -> new DeleteWebhookMessageUni(requester, webhookId, webhookToken, messageId));
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
