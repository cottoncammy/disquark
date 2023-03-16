package io.disquark.rest.webhook;

import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.webhook.DeleteWebhookMessageUni;
import io.disquark.rest.resources.webhook.EditWebhookMessageUni;
import io.disquark.rest.resources.webhook.ExecuteWebhookUni;
import io.disquark.rest.resources.webhook.GetWebhookMessageUni;
import io.disquark.rest.resources.webhook.ModifyWebhookWithTokenUni;
import io.disquark.rest.resources.webhook.Webhook;
import io.smallrye.mutiny.Uni;

public interface WebhooksCapable {

    Uni<Webhook> getWebhookWithToken(Snowflake webhookId, String webhookToken);

    ModifyWebhookWithTokenUni modifyWebhookWithToken(Snowflake webhookId, String webhookToken);

    Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken);

    ExecuteWebhookUni executeWebhook(Snowflake webhookId, String webhookToken);

    GetWebhookMessageUni getWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId);

    EditWebhookMessageUni editWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId);

    DeleteWebhookMessageUni deleteWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId);
}
