package org.example.rest;

import org.example.rest.request.channel.message.CreateMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class DiscordBotClientTest {

    @Test
    public void testCreateMessage() {
        DiscordBotClient bot;
        bot.createMessage(CreateMessage.builder()
                .content()
                .build());
    }
}
