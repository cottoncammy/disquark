package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordBotClient;
import org.example.rest.request.channel.message.CreateMessage;
import org.example.rest.resources.Snowflake;
import org.example.rest.response.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.example.it.ConfigHelper.configValue;

class DiscordBotClientIT {
    private static Snowflake channelId;
    private static DiscordBotClient<HttpResponse> botClient;

    @BeforeAll
    static void init() {
        channelId = configValue("DISCORD_CHANNEL_ID", Snowflake.class);
        botClient = DiscordBotClient.create(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class));
    }

    @Test
    void testCreateMessage() {
        CreateMessage createMessage = CreateMessage.builder().channelId(channelId).content("Hello World!").build();
        botClient.createMessage(createMessage)
                .onFailure().invoke(Throwable::printStackTrace)
                .subscribe().withSubscriber(UniAssertSubscriber.create()).awaitFailure();
    }
}
