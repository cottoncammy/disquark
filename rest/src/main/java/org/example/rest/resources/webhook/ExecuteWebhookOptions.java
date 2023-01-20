package org.example.rest.resources.webhook;

import java.util.Optional;

import org.example.immutables.ImmutableBuilder;
import org.example.rest.resources.Snowflake;
import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface ExecuteWebhookOptions {

    static Builder builder() {
        return new Builder();
    }

    static ExecuteWebhookOptions create(Snowflake webhookId, String webhookToken) {
        return ImmutableExecuteWebhookOptions.create(webhookId, webhookToken);
    }

    Snowflake webhookId();

    String webhookToken();

    Optional<Snowflake> threadId();

    @Default
    default boolean waitForServer() {
        return true;
    }

    class Builder extends ImmutableExecuteWebhookOptions.Builder {
        protected Builder() {
        }
    }
}
