package io.disquark.it;

import io.disquark.it.config.ConfigHelper;
import io.disquark.rest.AuthenticatedDiscordClient;
import io.disquark.rest.oauth2.DiscordOAuth2Client;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.application.command.BulkOverwriteGlobalApplicationCommands;
import io.disquark.rest.resources.application.command.BulkOverwriteGuildApplicationCommands;
import io.disquark.rest.resources.application.command.CreateGlobalApplicationCommand;
import io.disquark.rest.resources.application.command.CreateGuildApplicationCommand;
import io.disquark.rest.resources.application.command.EditGlobalApplicationCommand;
import io.disquark.rest.resources.application.command.EditGuildApplicationCommand;
import io.disquark.rest.resources.user.GetCurrentUserGuilds;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkParameterResolver.class)
class AuthenticatedDiscordClientIT {
    private Snowflake applicationId;
    private Snowflake guildId;

    private Snowflake globalCommandId;
    private Snowflake guildCommandId;

    @BeforeAll
    void init(DiscordOAuth2Client<?> oAuth2Client) {
        applicationId = oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .application()
                .id();

        guildId = ConfigHelper.configValue("DISCORD_GUILD_ID", Snowflake.class);

        oAuth2Client.getGlobalApplicationCommands(applicationId, false)
                .onItem()
                .transformToUniAndMerge(command -> oAuth2Client.deleteGlobalApplicationCommand(applicationId, command.id()))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem();

        oAuth2Client.getGuildApplicationCommands(applicationId, guildId, false)
                .onItem()
                .transformToUniAndMerge(
                        command -> oAuth2Client.deleteGuildApplicationCommand(applicationId, guildId, command.id()))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(1)
    void testGetGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommands(applicationId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(3)
    void testGetGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(5)
    void testBulkOverwriteGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        BulkOverwriteGlobalApplicationCommands bulkOverwriteGlobalApplicationCommands = BulkOverwriteGlobalApplicationCommands
                .builder()
                .applicationId(applicationId)
                .addGlobalApplicationCommandOverwrite(
                        BulkOverwriteGlobalApplicationCommands.GlobalApplicationCommandOverwrite.builder()
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(6)
    void testGetGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommands(applicationId, guildId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(8)
    void testGetGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(10)
    void testBulkOverwriteGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        BulkOverwriteGuildApplicationCommands bulkOverwriteGuildApplicationCommands = BulkOverwriteGuildApplicationCommands
                .builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .addGuildApplicationCommandOverwrite(
                        BulkOverwriteGuildApplicationCommands.GuildApplicationCommandOverwrite.builder()
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
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(11)
    void testGetGuildApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommandPermissions(applicationId, guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(12)
    void testGetApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getApplicationCommandPermissions(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(13)
    void testDeleteGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    @Order(14)
    void testDeleteGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    void testGetCurrentUser(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUser()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkTestTemplateProvider.class)
    void testGetCurrentUserGuilds(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUserGuilds(GetCurrentUserGuilds.create())
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
