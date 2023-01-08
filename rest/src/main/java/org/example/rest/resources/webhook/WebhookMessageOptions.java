package org.example.rest.resources.webhook;

import org.example.immutables.ImmutableBuilder;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

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
        protected Builder() {}
    }
}
