package org.example.it;

import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class ApplicationCommandIT extends AuthenticatedDiscordClientIT {

    @Test
    void testGetGlobalApplicationCommands(DiscordBotClient<?> botClient) {

    }

    @Test
    void testCreateGlobalApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetGlobalApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testEditGlobalApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteGlobalApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testBulkOverwriteGlobalApplicationCommands(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetGuildApplicationCommands(DiscordBotClient<?> botClient) {

    }

    @Test
    void testCreateGuildApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetGuildApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testEditGuildApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteGuildApplicationCommand(DiscordBotClient<?> botClient) {

    }

    @Test
    void testBulkOverwriteGuildApplicationCommands(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetGuildApplicationCommandPermissions(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetApplicationCommandPermissions(DiscordBotClient<?> botClient) {

    }
}
