package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.SomeExtension;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.GetCurrentUserGuilds;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

class AuthenticatedDiscordClientIT {

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testCreateGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testEditGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testDeleteGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testBulkOverwriteGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testCreateGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testEditGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testDeleteGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testBulkOverwriteGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetGuildApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {

    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUser(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUser().subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUserGuilds(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUserGuilds(GetCurrentUserGuilds.create())
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUserGuildMember(AuthenticatedDiscordClient<?> discordClient, Snowflake guildId) {
        discordClient.getCurrentUserGuildMember(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
