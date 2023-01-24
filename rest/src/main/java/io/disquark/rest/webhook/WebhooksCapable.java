package io.disquark.rest.webhook;

import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.webhook.EditWebhookMessage;
import io.disquark.rest.resources.webhook.ExecuteWebhook;
import io.disquark.rest.resources.webhook.ExecuteWebhookOptions;
import io.disquark.rest.resources.webhook.ModifyWebhookWithToken;
import io.disquark.rest.resources.webhook.Webhook;
import io.disquark.rest.resources.webhook.WebhookMessageOptions;
import io.smallrye.mutiny.Uni;

public interface WebhooksCapable {

    Uni<Webhook> getWebhookWithToken(Snowflake webhookId, String webhookToken);

    Uni<Webhook> modifyWebhookWithToken(ModifyWebhookWithToken modifyWebhookWithToken);

    Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken);

    Uni<Message> executeWebhook(ExecuteWebhook executeWebhook);

    Uni<Message> executeSlackCompatibleWebhook(ExecuteWebhookOptions options);

    Uni<Message> executeGitHubCompatibleWebhook(ExecuteWebhookOptions options);

    Uni<Message> getWebhookMessage(WebhookMessageOptions options);

    Uni<Message> editWebhookMessage(EditWebhookMessage editWebhookMessage);

    Uni<Void> deleteWebhookMessage(WebhookMessageOptions options);
}
