package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.channel.message.CreateMessage;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.EnumSet;

import static org.example.it.ConfigHelper.configValue;

@ExtendWith(SomeExtension2.class)
class DiscordBotClientIT {

    @BeforeAll
    static void init() {
        DiscordBotClient.create(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class));
    }

    @Test
    void testCreateMessage(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CHANNEL_ID") Snowflake channelId) {
        CreateMessage createMessage = CreateMessage.builder()
                .channelId(channelId)
                .content("Hello World!")
                .flags(EnumSet.of(Message.Flag.SUPPRESS_EMBEDS))
                .build();

        botClient.createMessage(createMessage).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
