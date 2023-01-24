package io.disquark.rest.resources.webhook;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.resources.Snowflake;

@ImmutableBuilder
public interface WebhookMessageOptions {

    static Builder builder() {
        return new Builder();
    }

    static WebhookMessageOptions create(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return ImmutableWebhookMessageOptions.create(webhookId, webhookToken, messageId);
    }

    Snowflake webhookId();

    String webhookToken();

    Snowflake messageId();

    Optional<Snowflake> threadId();

    class Builder extends ImmutableWebhookMessageOptions.Builder {
        protected Builder() {
        }
    }
}
