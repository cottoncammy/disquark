package io.disquark.it;

import io.disquark.it.config.ConfigHelper;
import io.disquark.rest.AuthenticatedDiscordClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.command.GlobalApplicationCommandOverwrite;
import io.disquark.rest.json.command.GuildApplicationCommandOverwrite;
import io.disquark.rest.oauth2.DiscordOAuth2Client;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
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
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(1)
    void testGetGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommands(applicationId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(2)
    void testCreateGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        globalCommandId = discordClient.createGlobalApplicationCommand(applicationId, "foo")
                .withDescription("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(3)
    void testGetGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(4)
    void testEditGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.editGlobalApplicationCommand(applicationId, globalCommandId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(5)
    void testBulkOverwriteGlobalApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.bulkOverwriteGlobalApplicationCommands(applicationId)
                .withOverwrites(new GlobalApplicationCommandOverwrite("bar").withDescription("foo bar"))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(6)
    void testGetGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommands(applicationId, guildId, false)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(7)
    void testCreateGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        guildCommandId = discordClient.createGuildApplicationCommand(applicationId, guildId, "foo")
                .withDescription("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(8)
    void testGetGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(9)
    void testEditGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.editGuildApplicationCommand(applicationId, guildId, globalCommandId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(10)
    void testBulkOverwriteGuildApplicationCommands(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.bulkOverwriteGuildApplicationCommands(applicationId, guildId)
                .withOverwrites(new GuildApplicationCommandOverwrite("bar").withDescription("foo bar"))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(11)
    void testGetGuildApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getGuildApplicationCommandPermissions(applicationId, guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(12)
    void testGetApplicationCommandPermissions(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getApplicationCommandPermissions(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(13)
    void testDeleteGlobalApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGlobalApplicationCommand(applicationId, globalCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    @Order(14)
    void testDeleteGuildApplicationCommand(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.deleteGuildApplicationCommand(applicationId, guildId, guildCommandId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    void testGetCurrentUser(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUser()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @TestTemplate
    @ExtendWith(DisQuarkJUnit5TestTemplateProvider.class)
    void testGetCurrentUserGuilds(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUserGuilds()
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
