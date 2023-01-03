package org.example.it;

import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class InviteIT {

    @Test
    void testGetInvite(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteInvite(DiscordBotClient<?> botClient) {

    }
}
