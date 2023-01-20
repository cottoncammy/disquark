package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.example.rest.resources.application.command.CreateGuildApplicationCommand;
import org.example.rest.resources.application.command.EditApplicationCommandPermissions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class DiscordOAuth2ClientIT {

    @Test
    void testEditApplicationCommandPermissions(DiscordOAuth2Client<?> oAuth2Client, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        Snowflake applicationId = oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .application().id();

        CreateGuildApplicationCommand createGuildApplicationCommand = CreateGuildApplicationCommand.builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .name("foo")
                .description("bar")
                .build();

        oAuth2Client.createGuildApplicationCommand(createGuildApplicationCommand)
                .call(command -> oAuth2Client.editApplicationCommandPermissions(EditApplicationCommandPermissions.builder()
                        .applicationId(applicationId)
                        .guildId(guildId)
                        .commandId(command.id())
                        .addPermission(ApplicationCommand.Permissions.builder()
                                .id(guildId)
                                .type(ApplicationCommand.Permissions.Type.ROLE)
                                .permission(true)
                                .build())
                                .build()))
                .onItemOrFailure().call((command, e) -> oAuth2Client.deleteGuildApplicationCommand(applicationId, guildId, command.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetCurrentUserGuildMember(DiscordOAuth2Client<?> oAuth2Client, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        oAuth2Client.getCurrentUserGuildMember(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetUserConnections(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getUserConnections()
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetCurrentAuthorizationInformation(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
