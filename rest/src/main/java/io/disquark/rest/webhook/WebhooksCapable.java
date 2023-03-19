package io.disquark.rest.webhook;

import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.webhook.DeleteWebhookMessageUni;
import io.disquark.rest.json.webhook.EditWebhookMessageUni;
import io.disquark.rest.json.webhook.ExecuteWebhookUni;
import io.disquark.rest.json.webhook.GetWebhookMessageUni;
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni;
import io.disquark.rest.json.webhook.Webhook;
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
