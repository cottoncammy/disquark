package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.*;
import org.example.rest.resources.user.GetCurrentUserGuilds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.example.it.config.ConfigHelper.configValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class AuthenticatedDiscordClientIT {
    private Snowflake applicationId;
    private Snowflake guildId;

    private Snowflake globalCommandId;
    private Snowflake guildCommandId;

    @BeforeAll
    void init(DiscordOAuth2Client<?> oAuth2Client) {
        applicationId = configValue("DISCORD_APPLICATION_ID", Snowflake.class);
        guildId = configValue("DISCORD_GUILD_ID", Snowflake.class);

        oAuth2Client.getGlobalApplicationCommands(applicationId, false)
                .onItem().transformToUniAndMerge(command -> oAuth2Client.deleteGlobalApplicationCommand(applicationId, command.id()))
                .collect().asList()
                .await().indefinitely();

        oAuth2Client.getGuildApplicationCommands(applicationId, guildId, false)
                .onItem().transformToUniAndMerge(command -> oAuth2Client.deleteGuildApplicationCommand(applicationId, guildId, command.id()))
                .collect().asList()
                .await().indefinitely();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(1)
    void testGetGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommands(applicationId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(2)
    void testCreateGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        CreateGlobalApplicationCommand createGlobalApplicationCommand = CreateGlobalApplicationCommand.builder()
                .applicationId(applicationId)
                .name("foo")
                .description("bar")
                .build();

        globalCommandId = discordClient.createGlobalApplicationCommand(createGlobalApplicationCommand)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(3)
    void testGetGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(4)
    void testEditGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        EditGlobalApplicationCommand editGlobalApplicationCommand = EditGlobalApplicationCommand.builder()
                .applicationId(applicationId)
                .commandId(globalCommandId)
                .name("bar")
                .build();

        discordClient.editGlobalApplicationCommand(editGlobalApplicationCommand)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(5)
    void testBulkOverwriteGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        BulkOverwriteGlobalApplicationCommands bulkOverwriteGlobalApplicationCommands = BulkOverwriteGlobalApplicationCommands.builder()
                .applicationId(applicationId)
                .addGlobalApplicationCommandOverwrite(BulkOverwriteGlobalApplicationCommands.GlobalApplicationCommandOverwrite.builder()
                        .name("bar")
                        .description("foo bar")
                        .build())
                .build();

        discordClient.bulkOverwriteGlobalApplicationCommands(bulkOverwriteGlobalApplicationCommands)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(6)
    void testGetGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommands(applicationId, guildId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(7)
    void testCreateGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        CreateGuildApplicationCommand createGuildApplicationCommand = CreateGuildApplicationCommand.builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .name("foo")
                .description("bar")
                .build();

        guildCommandId = discordClient.createGuildApplicationCommand(createGuildApplicationCommand)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(8)
    void testGetGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(9)
    void testEditGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        EditGuildApplicationCommand editGuildApplicationCommand = EditGuildApplicationCommand.builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .commandId(guildCommandId)
                .name("bar")
                .build();

        discordClient.editGuildApplicationCommand(editGuildApplicationCommand)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(10)
    void testBulkOverwriteGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        BulkOverwriteGuildApplicationCommands bulkOverwriteGuildApplicationCommands = BulkOverwriteGuildApplicationCommands.builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .addGuildApplicationCommandOverwrite(BulkOverwriteGuildApplicationCommands.GuildApplicationCommandOverwrite.builder()
                        .name("bar")
                        .description("foo bar")
                        .build())
                .build();

        discordClient.bulkOverwriteGuildApplicationCommands(bulkOverwriteGuildApplicationCommands)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(11)
    void testGetGuildApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommandPermissions(applicationId, guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(12)
    void testGetApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getApplicationCommandPermissions(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(13)
    void testDeleteGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    @Order(14)
    void testDeleteGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUser(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUser()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUserGuilds(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUserGuilds(GetCurrentUserGuilds.create())
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
