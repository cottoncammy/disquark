package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
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
class DiscordOAuth2ClientIT extends AuthenticatedDiscordClientIT {

    @Test
    void testEditApplicationCommandPermissions(DiscordOAuth2Client<?> oAuth2Client, @ConfigValue("DISCORD_APPLICATION_ID") Snowflake applicationId, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        CreateGuildApplicationCommand createGuildApplicationCommand = CreateGuildApplicationCommand.builder()
                .applicationId(applicationId)
                .guildId(guildId)
                .name("foo")
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
                                .build())
                        .onFailure().call(() -> oAuth2Client.deleteGuildApplicationCommand(applicationId, guildId, command.id())))
                .call(command -> oAuth2Client.deleteGuildApplicationCommand(applicationId, guildId, command.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    void testGetUserConnections(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getUserConnections().subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    void testGetCurrentAuthorizationInformation(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
