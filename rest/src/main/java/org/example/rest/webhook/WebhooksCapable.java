package org.example.rest.webhook;

import io.smallrye.mutiny.Uni;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.webhook.*;

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
