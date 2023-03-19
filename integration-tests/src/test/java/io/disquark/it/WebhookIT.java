package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.webhook.Webhook;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class WebhookIT {
    private Snowflake webhookId;
    private String webhookToken;
    private Snowflake messageId;

    @Test
    @Order(1)
    void testCreateWebhook(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        Webhook webhook = botClient.createWebhook(channelId, "foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        webhookId = webhook.id();
        webhookToken = webhook.token().get();
    }

    @Test
    @Order(2)
    void testGetChannelWebhooks(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        botClient.getChannelWebhooks(channelId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testGetGuildWebhooks(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildWebhooks(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetWebhook(DiscordBotClient<?> botClient) {
        botClient.getWebhook(webhookId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testGetWebhookWithToken(DiscordBotClient<?> botClient) {
        botClient.getWebhookWithToken(webhookId, webhookToken)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testModifyWebhook(DiscordBotClient<?> botClient) {
        botClient.modifyWebhook(webhookId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(7)
    void testModifyWebhookWithToken(DiscordBotClient<?> botClient) {
        botClient.modifyWebhookWithToken(webhookId, webhookToken)
                .withName("baz")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testExecuteWebhook(DiscordBotClient<?> botClient) {
        messageId = botClient.executeWebhook(webhookId, webhookToken)
                .withContent("Hello World!")
                .withWaitForServer(true)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(9)
    void testGetWebhookMessage(DiscordBotClient<?> botClient) {
        botClient.getWebhookMessage(webhookId, webhookToken, messageId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(10)
    void testEditWebhookMessage(DiscordBotClient<?> botClient) {
        botClient.editWebhookMessage(webhookId, webhookToken, messageId)
                .withContent("Goodbye Cruel World...")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(11)
    void testDeleteWebhookMessage(DiscordBotClient<?> botClient) {
        botClient.deleteWebhookMessage(webhookId, webhookToken, messageId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testDeleteWebhook(DiscordBotClient<?> botClient) {
        botClient.deleteWebhook(webhookId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(13)
    void testDeleteWebhookWithToken(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        botClient.createWebhook(channelId, "alice")
                .flatMap(webhook -> botClient.deleteWebhookWithToken(webhook.id(), webhook.token().get()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
