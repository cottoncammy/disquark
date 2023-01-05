package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.webhook.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class WebhookIT {
    private Snowflake webhookId;
    private String webhookToken;
    private Snowflake messageId;

    @Test
    @Order(1)
    void testCreateWebhook(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        Webhook webhook = botClient.createWebhook(CreateWebhook.builder().channelId(channelId).name("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem();

        webhookId = webhook.id();
        webhookToken = webhook.token().get();
    }

    @Test
    @Order(2)
    void testGetChannelWebhooks(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        botClient.getChannelWebhooks(channelId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(3)
    void testGetGuildWebhooks(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getGuildWebhooks(guildId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(4)
    void testGetWebhook(DiscordBotClient<?> botClient) {
        botClient.getWebhook(webhookId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(5)
    void testGetWebhookWithToken(DiscordBotClient<?> botClient) {
        botClient.getWebhookWithToken(webhookId, webhookToken)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testModifyWebhook(DiscordBotClient<?> botClient) {
        botClient.modifyWebhook(ModifyWebhook.builder().webhookId(webhookId).name("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(7)
    void testModifyWebhookWithToken(DiscordBotClient<?> botClient) {
        ModifyWebhookWithToken modifyWebhookWithToken = ModifyWebhookWithToken.builder()
                .webhookId(webhookId)
                .webhookToken(webhookToken)
                .name("baz")
                .build();

        botClient.modifyWebhookWithToken(modifyWebhookWithToken)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testExecuteWebhook(DiscordBotClient<?> botClient) {
        ExecuteWebhook executeWebhook = ExecuteWebhook.builder()
                .webhookId(webhookId)
                .webhookToken(webhookToken)
                .content("Hello World!")
                .build();

        messageId = botClient.executeWebhook(executeWebhook)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(9)
    void testGetWebhookMessage(DiscordBotClient<?> botClient) {
        botClient.getWebhookMessage(WebhookMessageOptions.create(webhookId, webhookToken, messageId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(10)
    void testEditWebhookMessage(DiscordBotClient<?> botClient) {
        EditWebhookMessage editWebhookMessage = EditWebhookMessage.builder()
                .webhookId(webhookId)
                .webhookToken(webhookToken)
                .messageId(messageId)
                .content("Goodbye Cruel World...")
                .build();

        botClient.editWebhookMessage(editWebhookMessage)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(11)
    void testDeleteWebhookMessage(DiscordBotClient<?> botClient) {
        botClient.deleteWebhookMessage(WebhookMessageOptions.create(webhookId, webhookToken, messageId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testDeleteWebhook(DiscordBotClient<?> botClient) {
        botClient.deleteWebhook(webhookId, null).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(13)
    void testDeleteWebhookWithToken(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        botClient.createWebhook(CreateWebhook.builder().channelId(channelId).name("alice").build())
                .flatMap(webhook -> botClient.deleteWebhookWithToken(webhook.id(), webhook.token().get()))
                .subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
